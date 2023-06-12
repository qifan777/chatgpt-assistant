package io.qifan.chatgpt.assistant.gpt.config;

import io.qifan.chatgpt.assistant.user.User;
import io.qifan.infrastructure.common.constants.BaseEnum;
import io.qifan.infrastructure.common.entity.BaseEntity;
import io.qifan.infrastructure.common.mongo.ValidStatus;
import io.qifan.infrastructure.generator.core.GenEntity;
import io.qifan.infrastructure.generator.core.GenField;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Arrays;

@GenEntity
@Document
@Accessors(chain = true)
@Data
public class ChatConfig extends BaseEntity {
    private GPTModel model;
    private Double temperature;
    private Integer maxTokens;
    private Double presencePenalty;
    private String apiKey;
    @GenField(association = true, ignoreRequest = true)
    @CreatedBy
    @DBRef
    private User createdBy;
    @GenField(ignoreRequest = true)
    private ValidStatus validStatus;


    public void valid() {
        setValidStatus(ValidStatus.VALID);
    }

    public void invalid() {
        setValidStatus(ValidStatus.INVALID);
    }

    public enum GPTModel implements BaseEnum {
        GPT_35_TURBO(0, "gpt-3.5-turbo"),
        GPT_4(1, "gpt-4");

        private final Integer code;
        private final String name;

        GPTModel(Integer code, String name) {
            this.code = code;
            this.name = name;
        }

        public static GPTModel nameOf(String name) {
            return Arrays.stream(GPTModel.values()).filter(type -> type.getName().equals(name))
                         .findFirst()
                         .orElseThrow(() -> new RuntimeException("枚举不存在"));
        }

        public static GPTModel codeOf(Integer code) {
            return Arrays.stream(GPTModel.values()).filter(type -> type.getCode().equals(code))
                         .findFirst()
                         .orElseThrow(() -> new RuntimeException("枚举不存在"));
        }

        @Override
        public Integer getCode() {
            return this.code;
        }

        @Override
        public String getName() {
            return this.name;
        }
    }

}
