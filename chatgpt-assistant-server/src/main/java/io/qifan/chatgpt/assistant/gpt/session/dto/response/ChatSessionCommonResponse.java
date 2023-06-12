package io.qifan.chatgpt.assistant.gpt.session.dto.response;

import io.qifan.chatgpt.assistant.gpt.session.ChatSession.Statistic;
import io.qifan.chatgpt.assistant.user.dto.response.UserCommonResponse;
import io.qifan.infrastructure.common.model.BaseResponse;
import io.qifan.infrastructure.common.mongo.ValidStatus;
import lombok.Data;


@Data
public class ChatSessionCommonResponse extends BaseResponse {
    private String topic;
    private Statistic statistic;
    private UserCommonResponse createdBy;
    private ValidStatus validStatus;
}