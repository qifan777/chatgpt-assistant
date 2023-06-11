package io.qifan.chatgpt.assistant.gpt.dto.request;

import io.qifan.chatgpt.assistant.gpt.ChatConfig.GPTModel;
import io.qifan.infrastructure.common.model.BaseRequest;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;


@Data
public class ChatConfigCreateRequest extends BaseRequest {
    private GPTModel model = GPTModel.GPT_35_TURBO;
    @Min(value = 0, message = "随机性不能小于0")
    @Max(value = 1, message = "随机性不能大于0")
    private Double temperature = 0.;
    private Integer maxTokens = 2000;
    @Min(value = -2, message = "话题新鲜度不能小于-2")
    @Max(value = 2, message = "话题新鲜度不能大于2")
    private Double presencePenalty = 0.;
    private String apiKey;
}