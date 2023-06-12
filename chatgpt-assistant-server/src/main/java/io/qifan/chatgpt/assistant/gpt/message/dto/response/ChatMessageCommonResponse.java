package io.qifan.chatgpt.assistant.gpt.message.dto.response;

import io.qifan.chatgpt.assistant.gpt.session.dto.response.ChatSessionCommonResponse;
import io.qifan.infrastructure.common.model.BaseResponse;
import io.qifan.infrastructure.common.mongo.ValidStatus;
import lombok.Data;


@Data
public class ChatMessageCommonResponse extends BaseResponse {
    private String content;
    private String role;
    private ChatSessionCommonResponse session;
    private ValidStatus validStatus;
}