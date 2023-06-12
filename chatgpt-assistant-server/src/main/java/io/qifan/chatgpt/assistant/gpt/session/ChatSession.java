package io.qifan.chatgpt.assistant.gpt.session;


import io.qifan.chatgpt.assistant.user.User;
import io.qifan.infrastructure.common.entity.BaseEntity;
import io.qifan.infrastructure.common.mongo.ValidStatus;
import io.qifan.infrastructure.generator.core.GenEntity;
import io.qifan.infrastructure.generator.core.GenField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.mongodb.core.mapping.Document;

@GenEntity
@Document
@Accessors(chain = true)
@Data
public class ChatSession extends BaseEntity {
    private String topic;
    private Statistic statistic;
    @GenField(association = true, ignoreRequest = true)
    @CreatedBy
    private User createdBy;
    @GenField(ignoreRequest = true)
    private ValidStatus validStatus;


    public void valid() {
        setValidStatus(ValidStatus.VALID);
    }

    public void invalid() {
        setValidStatus(ValidStatus.INVALID);
    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Statistic {
        private Integer charCount;
        private Integer tokenCount;
        private Integer wordCount;
    }
}
