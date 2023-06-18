package io.qifan.chatgpt.assistant.gpt.message;


import io.qifan.chatgpt.assistant.gpt.session.ChatSession;
import io.qifan.infrastructure.common.entity.BaseEntity;
import io.qifan.infrastructure.common.mongo.ValidStatus;
import io.qifan.infrastructure.generator.core.GenEntity;
import io.qifan.infrastructure.generator.core.GenField;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@GenEntity
@Document
@Accessors(chain = true)
@Data
public class ChatMessage extends BaseEntity {
    private String content;
    // user assistant
    private String role;
    @GenField(association = true)
    @DBRef
    private ChatSession session;
    @GenField(ignoreRequest = true)
    private ValidStatus validStatus;


    public void valid() {
        setValidStatus(ValidStatus.VALID);
    }

    public void invalid() {
        setValidStatus(ValidStatus.INVALID);
    }
}
