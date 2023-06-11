package io.qifan.chatgpt.assistant.gpt.session.dto.response;

import io.qifan.chatgpt.assistant.gpt.ChatConfig;
import io.qifan.chatgpt.assistant.gpt.session.ChatSession.Message;
import io.qifan.chatgpt.assistant.gpt.session.ChatSession.Statistic;
import io.qifan.chatgpt.assistant.user.dto.response.UserCommonResponse;
import io.qifan.infrastructure.common.model.BaseResponse;
import io.qifan.infrastructure.common.mongo.ValidStatus;
import lombok.Data;

import java.util.List;


@Data
public class ChatSessionCommonResponse extends BaseResponse {
    private ChatConfig config;
    private String topic;
    private Statistic statistic;
    private List<Message> messages;
    private UserCommonResponse createdBy;
    private ValidStatus validStatus;
}