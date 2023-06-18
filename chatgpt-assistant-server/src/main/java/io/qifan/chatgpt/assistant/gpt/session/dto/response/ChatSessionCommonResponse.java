package io.qifan.chatgpt.assistant.gpt.session.dto.response;

import io.qifan.chatgpt.assistant.gpt.message.dto.response.ChatMessageCommonResponse;
import io.qifan.chatgpt.assistant.gpt.session.ChatSession.Statistic;
import io.qifan.chatgpt.assistant.user.dto.response.UserCommonResponse;
import io.qifan.infrastructure.common.model.BaseResponse;
import io.qifan.infrastructure.common.mongo.ValidStatus;
import lombok.Data;

import java.util.List;


@Data
public class ChatSessionCommonResponse extends BaseResponse {
    private String topic;
    private Statistic statistic;
    private List<ChatMessageCommonResponse> messages;
    private UserCommonResponse createdBy;
    private ValidStatus validStatus;
}