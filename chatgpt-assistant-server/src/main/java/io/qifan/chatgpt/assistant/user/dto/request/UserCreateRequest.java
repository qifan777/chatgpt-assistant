package io.qifan.chatgpt.assistant.user.dto.request;

import io.qifan.infrastructure.common.model.BaseRequest;
import lombok.Data;

@Data
public class UserCreateRequest extends BaseRequest {
    private String avatar;
    private String nickname;
    private String username;
    private String password;
}