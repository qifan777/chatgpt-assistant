package io.qifan.chatgpt.assistant.infrastructure.gpt;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "gpt")
@Component
@Data
public class GPTProperty {
    Proxy proxy;

    @Data
    public static class Proxy {
        private String host;
        private Integer port;
    }
}