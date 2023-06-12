package io.qifan.chatgpt.assistant.gpt.message.dto.request;

import io.qifan.chatgpt.assistant.gpt.session.dto.request.ChatSessionUpdateRequest;
import io.qifan.infrastructure.common.model.BaseRequest;
import lombok.Data;


@Data
public class ChatMessageUpdateRequest extends BaseRequest {
    private String content;
    private String role;
    private ChatSessionUpdateRequest session;
}