package io.qifan.chatgpt.assistant.gpt.dto.request;

import io.qifan.chatgpt.assistant.gpt.ChatConfig.GPTModel;
import io.qifan.infrastructure.common.model.BaseRequest;
import lombok.Data;


@Data
public class ChatConfigQueryRequest extends BaseRequest {
    private GPTModel model;
    private Double temperature;
    private Integer maxTokens;
    private Double presencePenalty;
    private String apiKey;
}