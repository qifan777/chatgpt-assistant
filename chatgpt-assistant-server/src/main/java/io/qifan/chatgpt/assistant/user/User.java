package io.qifan.chatgpt.assistant.user;

import io.qifan.infrastructure.common.entity.BaseEntity;
import io.qifan.infrastructure.common.mongo.ValidStatus;
import io.qifan.infrastructure.generator.core.GenEntity;
import io.qifan.infrastructure.generator.core.GenField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.mapping.Document;

@EqualsAndHashCode(callSuper = true)
@GenEntity
@Document
@Accessors(chain = true)
@Data
public class User extends BaseEntity {
    @GenField(ignoreRequest = true)
    private ValidStatus validStatus;


    public void valid() {
        setValidStatus(ValidStatus.VALID);
    }

    public void invalid() {
        setValidStatus(ValidStatus.INVALID);
    }
}