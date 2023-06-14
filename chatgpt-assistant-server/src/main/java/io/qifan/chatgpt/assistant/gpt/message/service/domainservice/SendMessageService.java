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
    private final ChatMessageMapper chatMessageMapper;
    private final ChatSessionRepository chatSessionRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final MongoTemplate mongoTemplate;
    private final GPTProperty property;
    private final SimpMessagingTemplate messagingTemplate;

    public ChatConfig checkConfig(Principal principal) {
        ChatConfig chatConfig = Optional.ofNullable(mongoTemplate.findOne(Query.query(Criteria.where("createdBy.id")
                                                                                              .is(principal.getName())),
                                                                          ChatConfig.class))
                                        .orElseThrow(() -> new BusinessException(ResultCode.NotFindError,
                                                                                 "请配置API Key"));
        if (!StringUtils.hasText(chatConfig.getApiKey())) {
            throw new BusinessException(ResultCode.ValidateError, "请配置API Key");
        }
        return chatConfig;
    }

    public OpenAiService createOpenAIService(ChatConfig chatConfig) {
        ObjectMapper mapper = defaultObjectMapper();
        Proxy proxy = new Proxy(Proxy.Type.HTTP,
                                new InetSocketAddress(property.getProxy().getHost(), property.getProxy().getPort()));
        OkHttpClient client = defaultClient(chatConfig.getApiKey(), Duration.ofMinutes(1))
                .newBuilder()
                .proxy(proxy)
                .build();
        Retrofit retrofit = defaultRetrofit(client, mapper);
        OpenAiApi api = retrofit.create(OpenAiApi.class);
        return new OpenAiService(api);
    }

    public ChatCompletionRequest createChatRequest(ChatMessage chatMessage, ChatConfig chatConfig) {
        List<ChatMessage> chatMessageList = mongoTemplate.find(Query.query(Criteria.where("session.id")
                                                                                   .is(chatMessage.getSession()
                                                                                                  .getId())),
                                                               ChatMessage.class);

        chatMessageList.add(chatMessage);
        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                                                                           .messages(chatMessageList.stream()
                                                                                                    .map(chatMessageMapper::entityToMessage)
                                                                                                    .collect(Collectors.toList()))
                                                                           .model(chatConfig.getModel().getName())
                                                                           .presencePenalty(chatConfig.getPresencePenalty())
                                                                           .temperature(chatConfig.getTemperature())
                                                                           .maxTokens(chatConfig.getMaxTokens())
                                                                           .stream(true)
                                                                           .build();


        log.info("请求体：{}", chatCompletionRequest);
        return chatCompletionRequest;
    }

    @SneakyThrows
    public void sendMessage(OpenAiService service,
                            ChatCompletionRequest chatCompletionRequest,
                            ChatMessage chatMessage,
                            ChatSession chatSession,
                            Principal principal) {
        ChatSession.Statistic statistic = chatSession.getStatistic();
        statistic.setCharCount(statistic.getCharCount() + chatMessage.getContent().length());
        statistic.setTokenCount(statistic.getTokenCount() + chatMessage.getContent().length());
        ChatMessage responseMessage = new ChatMessage().setContent("").setRole("assistant").setSession(chatSession);
        service.streamChatCompletion(chatCompletionRequest)
               .doOnError(Throwable::printStackTrace)
               .blockingForEach(chunk -> {
                   log.info(chunk.toString());
                   String text = chunk.getChoices().get(0).getMessage().getContent();
                   if (text == null) {
                       return;
                   }
                   statistic.setTokenCount(statistic.getTokenCount() + 1);
                   statistic.setCharCount(statistic.getCharCount() + chatMessage.getContent().length());
                   messagingTemplate.convertAndSendToUser(principal.getName(),
                                                          "/queue/chatMessage/receive",
                                                          text);
                   responseMessage.setContent(responseMessage.getContent() + text);
                   log.info(text);
               });
        chatMessageRepository.save(chatMessage);
        chatMessageRepository.save(responseMessage);
        chatSessionRepository.save(chatSession);
    }
}
