package io.qifan.chatgpt.assistant.gpt.session.dto.request;

import io.qifan.chatgpt.assistant.gpt.ChatConfig;
import io.qifan.chatgpt.assistant.gpt.session.ChatSession.Message;
import io.qifan.chatgpt.assistant.gpt.session.ChatSession.Statistic;
import io.qifan.infrastructure.common.model.BaseRequest;
import lombok.Data;

import java.util.List;


@Data
public class ChatSessionQueryRequest extends BaseRequest {
    private ChatConfig config;
    private String topic;
    private Statistic statistic;
    private List<Message> messages;
}