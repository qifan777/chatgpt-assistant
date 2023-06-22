package io.qifan.chatgpt.assistant.gpt.message.service.domainservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.theokanning.openai.OpenAiApi;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.service.OpenAiService;
import io.qifan.chatgpt.assistant.gpt.config.ChatConfig;
import io.qifan.chatgpt.assistant.gpt.message.ChatMessage;
import io.qifan.chatgpt.assistant.gpt.message.mapper.ChatMessageMapper;
import io.qifan.chatgpt.assistant.gpt.message.repository.ChatMessageRepository;
import io.qifan.chatgpt.assistant.gpt.session.ChatSession;
import io.qifan.chatgpt.assistant.gpt.session.repository.ChatSessionRepository;
import io.qifan.chatgpt.assistant.infrastructure.gpt.GPTProperty;
import io.qifan.infrastructure.common.constants.ResultCode;
import io.qifan.infrastructure.common.exception.BusinessException;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import retrofit2.Retrofit;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.security.Principal;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.theokanning.openai.service.OpenAiService.*;


@Service
@AllArgsConstructor
@Slf4j
public class SendMessageService {
    private final ChatSessionRepository chatSessionRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final MongoTemplate mongoTemplate;
    private final GPTProperty gptProperty;
    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageMapper chatMessageMapper;

    /**
     * 校验用户是否存在GPT配置以及GPT配置中是否已经配置了API Key
     *
     * @param principal 握手阶段得到的用户信息
     * @return 该用户的GPT配置
     */
    public ChatConfig checkConfig(Principal principal) {
        log.info("GPT配置校验，当前用户：{}", principal);
        ChatConfig chatConfig = Optional.ofNullable(mongoTemplate.findOne(Query.query(Criteria.where("createdBy.id")
                                                                                              .is(principal.getName())),
                                                                          ChatConfig.class))
                                        .orElseThrow(() -> new BusinessException(ResultCode.NotFindError,
                                                                                 "请配置API Key"));
        if (!StringUtils.hasText(chatConfig.getApiKey())) {
            throw new BusinessException(ResultCode.ValidateError, "请配置API Key");
        }
        log.info("GPT配置校验通过，配置内容：{}", chatConfig);
        return chatConfig;
    }

    /**
     * @param chatConfig 用户的GPT配置
     * @return OpenAIService用于调用OpenAI接口
     */
    public OpenAiService createOpenAIService(ChatConfig chatConfig) {
        log.info("开始创建OpenAIService");
        ObjectMapper mapper = defaultObjectMapper();
        Proxy proxy = new Proxy(Proxy.Type.HTTP,
                                new InetSocketAddress(gptProperty.getProxy().getHost(),
                                                      gptProperty.getProxy().getPort()));
        OkHttpClient client = defaultClient(chatConfig.getApiKey(), Duration.ofMinutes(1))
                .newBuilder()
                .proxy(proxy)
                .build();
        Retrofit retrofit = defaultRetrofit(client, mapper);
        OpenAiApi api = retrofit.create(OpenAiApi.class);
        return new OpenAiService(api);
    }

    /**
     * 构造ChatGPT请求参数
     *
     * @param chatMessage 用户的发送内容
     * @param chatConfig  用户的GPT配置信息
     * @return 返回包含用户发送内容+配置信息的ChatGPT请求参数。
     */
    public ChatCompletionRequest createChatRequest(ChatMessage chatMessage, ChatConfig chatConfig) {
        List<ChatMessage> chatMessageList = mongoTemplate.find(Query.query(Criteria.where("session.id")
                                                                                   .is(chatMessage.getSession()
                                                                                                  .getId())),
                                                               ChatMessage.class);

        chatMessageList.add(chatMessage);

        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                                                                           .messages(chatMessageList.stream()
                                                                                                    .map(chatMessageMapper::entityToMessage)
                                                                                                    .collect(
                                                                                                            Collectors.toList()))
                                                                           .model(chatConfig.getModel().getName())
                                                                           .presencePenalty(
                                                                                   chatConfig.getPresencePenalty())
                                                                           .temperature(chatConfig.getTemperature())
                                                                           .maxTokens(chatConfig.getMaxTokens())
                                                                           .stream(true)
                                                                           .build();


        log.info("请求体：{}", chatCompletionRequest);
        return chatCompletionRequest;
    }

    /**
     * 向OpenAI发起ChatGPT请求，并将响应的结果推送给前端。
     * @param openAiService 封装好的OpenAI的服务，调用就可以发起请求。
     * @param chatCompletionRequest ChatGPT请求参数
     * @param chatMessage 用户发送的消息内容
     * @param chatSession 消息归属的会话
     * @param principal 当前用户信息
     */
    @SneakyThrows
    public void sendMessage(OpenAiService openAiService,
                            ChatCompletionRequest chatCompletionRequest,
                            ChatMessage chatMessage,
                            ChatSession chatSession,
                            Principal principal) {
        ChatSession.Statistic statistic = chatSession.getStatistic()
                                                     .plusChar(chatMessage.getContent().length())
                                                     .plusToken(chatMessage.getContent().length());

        ChatMessage responseMessage = new ChatMessage().setContent("")
                                                       .setRole("assistant")
                                                       .setSession(chatSession);
        openAiService.streamChatCompletion(chatCompletionRequest)
               .doOnError(Throwable::printStackTrace)
               .blockingForEach(chunk -> {
                   log.info(chunk.toString());
                   String text = chunk.getChoices().get(0).getMessage().getContent();
                   if (text == null) {
                       return;
                   }
                   statistic.plusToken(1)
                            .plusChar(text.length());
                   messagingTemplate.convertAndSendToUser(principal.getName(),
                                                          "/queue/chatMessage/receive",
                                                          text);
                   responseMessage.setContent(responseMessage.getContent() + text);
               });
        chatMessageRepository.save(chatMessage);
        chatMessageRepository.save(responseMessage);
        chatSessionRepository.save(chatSession);
    }
}
