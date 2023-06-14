package io.qifan.chatgpt.assistant;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.theokanning.openai.OpenAiApi;
import com.theokanning.openai.completion.chat.ChatCompletionChunk;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;
import io.qifan.chatgpt.assistant.gpt.session.ChatSession;
import io.qifan.chatgpt.assistant.infrastructure.gpt.GPTProperty;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import retrofit2.Retrofit;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import static com.theokanning.openai.service.OpenAiService.*;

@SpringBootTest
@Slf4j
public class ApplicationTest {
    @Autowired
    GPTProperty property;
    @Autowired
    MongoTemplate mongoTemplate;

    @Test
    void findById() {
        List<ChatSession> all = mongoTemplate.findAll(ChatSession.class);
        all.forEach(chatSession -> log.info(chatSession.toString()));
    }

    @SneakyThrows
    @Test
    void chatTest() {
        ObjectMapper mapper = defaultObjectMapper();
        Proxy proxy = new Proxy(Proxy.Type.HTTP,
                                new InetSocketAddress(property.getProxy().getHost(), property.getProxy().getPort()));
        OkHttpClient client = defaultClient("",
                                            Duration.ofMinutes(1))
                .newBuilder()
                .proxy(proxy)
                .build();
        Retrofit retrofit = defaultRetrofit(client, mapper);
        OpenAiApi api = retrofit.create(OpenAiApi.class);
        OpenAiService service = new OpenAiService(api);

        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                                                                           .messages(List.of(new ChatMessage("user",
                                                                                                             "你好")))
                                                                           .model("gpt-3.5-turbo")
//                                                                           .model(chatConfig.getModel().getName())
//                                                                           .presencePenalty(chatConfig.getPresencePenalty())
//                                                                           .temperature(chatConfig.getTemperature())
//                                                                           .maxTokens(chatConfig.getMaxTokens())
                                                                           .stream(true)
                                                                           .build();
        CountDownLatch countDownLatch = new CountDownLatch(1);
        service.streamChatCompletion(chatCompletionRequest).subscribe(new Subscriber<>() {
            private Subscription subscription;
            private io.qifan.chatgpt.assistant.gpt.message.ChatMessage responseChatMessage;

            @Override
            public void onSubscribe(Subscription subscription) {
                this.subscription = subscription;
                subscription.request(1);
                responseChatMessage = new io.qifan.chatgpt.assistant.gpt.message.ChatMessage().setContent("");
                log.info("订阅");
            }

            @Override
            public void onNext(ChatCompletionChunk chatCompletionChunk) {
                com.theokanning.openai.completion.chat.ChatMessage chatMessage = chatCompletionChunk.getChoices().get(0)
                                                                                                    .getMessage();
                if (chatMessage.getContent() != null) {
                    log.info("收到响应消息：{}", chatMessage);
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
                log.info("请求结束");
                countDownLatch.countDown();
            }
        });
        countDownLatch.await();
    }
}
