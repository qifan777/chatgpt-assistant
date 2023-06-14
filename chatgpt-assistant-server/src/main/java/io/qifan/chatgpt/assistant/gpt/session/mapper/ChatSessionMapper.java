package io.qifan.chatgpt.assistant.gpt.session.mapper;

import io.qifan.chatgpt.assistant.gpt.message.ChatMessage;
import io.qifan.chatgpt.assistant.gpt.message.dto.response.ChatMessageCommonResponse;
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

    @Mapping(target = "messages", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_DEFAULT)
    ChatSessionCommonResponse entity2Response(ChatSession entity);

    @Mapping(target = "session", ignore = true)
    ChatMessageCommonResponse entity2Response(ChatMessage message);
}