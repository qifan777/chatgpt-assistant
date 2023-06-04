package io.qifan.chatgpt.assistant.user.dto.response;

import io.qifan.infrastructure.common.model.BaseResponse;
import io.qifan.infrastructure.common.mongo.ValidStatus;
import lombok.Data;


@Data
public class UserCommonResponse extends BaseResponse {
    private String avatar;
    private String nickname;
    private String username;
    private String password;
    private ValidStatus validStatus;
}