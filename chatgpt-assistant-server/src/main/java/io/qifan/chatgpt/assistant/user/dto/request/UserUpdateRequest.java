package io.qifan.chatgpt.assistant.user.dto.request;

import io.qifan.infrastructure.common.model.BaseRequest;
import lombok.Data;

@Data
public class UserUpdateRequest extends BaseRequest {
    private String avatar;
    private String nickname;
}