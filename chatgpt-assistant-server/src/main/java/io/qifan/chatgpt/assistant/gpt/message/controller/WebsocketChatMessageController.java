package io.qifan.chatgpt.assistant.gpt.message.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.theokanning.openai.OpenAiApi;
import com.theokanning.openai.completion.chat.ChatCompletionChunk;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.service.OpenAiService;
import io.qifan.chatgpt.assistant.gpt.config.ChatConfig;
import io.qifan.chatgpt.assistant.gpt.message.ChatMessage;
import io.qifan.chatgpt.assistant.gpt.message.dto.request.ChatMessageCreateRequest;
import io.qifan.chatgpt.assistant.gpt.message.mapper.ChatMessageMapper;
import io.qifan.chatgpt.assistant.gpt.message.repository.ChatMessageRepository;
import io.qifan.chatgpt.assistant.gpt.session.ChatSession;
import io.qifan.chatgpt.assistant.gpt.session.repository.ChatSessionRepository;
import io.qifan.chatgpt.assistant.infrastructure.gpt.GPTProperty;
import io.qifan.infrastructure.common.constants.ResultCode;
import io.qifan.infrastructure.common.exception.BusinessException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
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

@Controller
@AllArgsConstructor
@Slf4j
public class WebsocketChatMessageController {
    private final ChatMessageMapper chatMessageMapper;
    private final GPTProperty property;
    private final SimpMessagingTemplate template;
    private final MongoTemplate mongoTemplate;
    private final ChatSessionRepository chatSessionRepository;
    private final ChatMessageRepository chatMessageRepository;

    @MessageMapping("/chatMessage/send")
    public void chat(@Payload ChatMessageCreateRequest requestMessage, Principal principal) {
        ChatConfig chatConfig = Optional.ofNullable(mongoTemplate.findOne(Query.query(Criteria.where("createdBy.id")
                                                                                              .is(principal.getName())),
                                                                          ChatConfig.class))
                                        .orElseThrow(() -> new BusinessException(ResultCode.NotFindError,
                                                                                 "请配置API Key"));
        if (!StringUtils.hasText(chatConfig.getApiKey())) {
            throw new BusinessException(ResultCode.ValidateError, "请配置API Key");
        }
        ChatSession chatSession = chatSessionRepository.findById(requestMessage.getSession().getId())
                                                       .orElseThrow(() -> new BusinessException(ResultCode.NotFindError));
        ObjectMapper mapper = defaultObjectMapper();
        Proxy proxy = new Proxy(Proxy.Type.HTTP,
                                new InetSocketAddress(property.getProxy().getHost(), property.getProxy().getPort()));
        OkHttpClient client = defaultClient(chatConfig.getApiKey(), Duration.ofMinutes(1))
                .newBuilder()
                .proxy(proxy)
                .build();
        Retrofit retrofit = defaultRetrofit(client, mapper);
        OpenAiApi api = retrofit.create(OpenAiApi.class);
        OpenAiService service = new OpenAiService(api);
        List<ChatMessage> chatMessageList = mongoTemplate.find(Query.query(Criteria.where("session.id")
                                                                                   .is(requestMessage.getSession()
                                                                                                     .getId())),
                                                               ChatMessage.class);
        chatMessageList.add(chatMessageMapper.createRequest2Entity(requestMessage));
        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                                                                           .messages(chatMessageList
                                                                                             .stream()
                                                                                             .map(chatMessageMapper::entityToMessage)
                                                                                             .collect(Collectors.toList()))
                                                                           .model(chatConfig.getModel().getName())
                                                                           .frequencyPenalty(chatConfig.getPresencePenalty())
                                                                           .temperature(chatConfig.getTemperature())
                                                                           .maxTokens(chatConfig.getMaxTokens())
                                                                           .stream(true)
                                                                           .build();
        ChatSession.Statistic statistic = chatSession.getStatistic();
        statistic.setCharCount(statistic.getCharCount() + requestMessage.getContent().length());
        statistic.setTokenCount(statistic.getTokenCount() + requestMessage.getContent().length());
        service.streamChatCompletion(chatCompletionRequest).subscribe(new Subscriber<>() {
            private Subscription subscription;
            private ChatMessage responseChatMessage;

            @Override
            public void onSubscribe(Subscription subscription) {
                this.subscription = subscription;
                subscription.request(1);
                responseChatMessage = new ChatMessage().setContent("");
            }

            @Override
            public void onNext(ChatCompletionChunk chatCompletionChunk) {
                com.theokanning.openai.completion.chat.ChatMessage chatMessage = chatCompletionChunk.getChoices()
                                                                                                    .get(0)
                                                                                                    .getMessage();
                statistic.setTokenCount(statistic.getTokenCount() + 1);
                statistic.setCharCount(statistic.getCharCount() + chatMessage.getContent().length());
                if (chatMessage.getContent() != null) {
                    template.convertAndSendToUser(principal.getName(),
                                                  "/queue/chatMessage/receive",
                                                  chatMessage.getContent());
                    responseChatMessage.setContent(responseChatMessage.getContent() + chatMessage.getContent());
                    responseChatMessage.setRole(chatMessage.getRole());

                }
                subscription.request(1);

            }

            @Override
            public void onError(Throwable throwable) {
                throwable.printStackTrace();
            }

            @Override
            public void onComplete() {
                chatMessageRepository.save(chatMessageList.get(chatMessageList.size() - 1));
                chatMessageRepository.save(responseChatMessage);
                chatSessionRepository.save(chatSession);
            }
        });
    }
}
