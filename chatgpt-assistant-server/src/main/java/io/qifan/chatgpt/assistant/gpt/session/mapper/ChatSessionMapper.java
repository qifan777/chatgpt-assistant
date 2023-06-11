package io.qifan.chatgpt.assistant.gpt.session.mapper;

import io.qifan.chatgpt.assistant.gpt.session.ChatSession;
import io.qifan.chatgpt.assistant.gpt.session.dto.request.ChatSessionCreateRequest;
import io.qifan.chatgpt.assistant.gpt.session.dto.request.ChatSessionQueryRequest;
import io.qifan.chatgpt.assistant.gpt.session.dto.request.ChatSessionUpdateRequest;
import io.qifan.chatgpt.assistant.gpt.session.dto.response.ChatSessionCommonResponse;
import io.qifan.chatgpt.assistant.infrastructure.mapper.CustomMapper;
import org.mapstruct.*;

@Mapper(
        uses = {CustomMapper.class},
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING
)
public interface ChatSessionMapper {

    ChatSession createRequest2Entity(ChatSessionCreateRequest request);

    ChatSession queryRequest2Entity(ChatSessionQueryRequest request);

    ChatSession updateEntityFromUpdateRequest(ChatSessionUpdateRequest request, @MappingTarget ChatSession entity);

    ChatSessionCommonResponse entity2Response(ChatSession entity);
}