package io.qifan.chatgpt.assistant.gpt.session.dto.request;

import io.qifan.chatgpt.assistant.gpt.session.ChatSession.Statistic;
import io.qifan.infrastructure.common.model.BaseRequest;
import lombok.Data;


@Data
public class ChatSessionUpdateRequest extends BaseRequest {
    private String topic;
    private Statistic statistic;
}