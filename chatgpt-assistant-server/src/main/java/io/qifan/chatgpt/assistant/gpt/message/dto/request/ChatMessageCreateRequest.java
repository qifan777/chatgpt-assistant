package io.qifan.chatgpt.assistant.gpt.message.dto.request;

import io.qifan.chatgpt.assistant.gpt.session.dto.request.ChatSessionCreateRequest;
import io.qifan.infrastructure.common.model.BaseRequest;
import lombok.Data;


@Data
public class ChatMessageCreateRequest extends BaseRequest {
    private String content;
    private String role;
    private ChatSessionCreateRequest session;
}