package io.qifan.chatgpt.assistant.infrastructure.config;

import cn.dev33.satoken.stp.StpUtil;

import io.qifan.chatgpt.assistant.user.User;
import io.qifan.chatgpt.assistant.user.dto.response.UserCommonResponse;
import io.qifan.chatgpt.assistant.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.Optional;

@Configuration
@EnableMongoAuditing
@AllArgsConstructor
public class MongoAuditingConfig {
    private final UserRepository userRepository;


    @Bean
    public AuditorAware<User> auditorAware() {
        return () -> {
            if (RequestContextHolder.getRequestAttributes() == null) return Optional.empty();
            Object loginIdDefaultNull = StpUtil.getLoginIdDefaultNull();
            if (loginIdDefaultNull != null) {
                return userRepository.findById(StpUtil.getLoginIdAsString());
            }
            return Optional.empty();
        };

    }
}
