package io.qifan.chatgpt.assistant.gpt.message.mapper;

import io.qifan.chatgpt.assistant.gpt.message.ChatMessage;
import io.qifan.chatgpt.assistant.gpt.message.dto.request.ChatMessageCreateRequest;
import io.qifan.chatgpt.assistant.gpt.message.dto.request.ChatMessageQueryRequest;
import io.qifan.chatgpt.assistant.gpt.message.dto.request.ChatMessageUpdateRequest;
import io.qifan.chatgpt.assistant.gpt.message.dto.response.ChatMessageCommonResponse;
import io.qifan.chatgpt.assistant.infrastructure.mapper.CustomMapper;
import org.mapstruct.*;

@Mapper(
        uses = {CustomMapper.class},
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING
)
public interface ChatMessageMapper {

    ChatMessage createRequest2Entity(ChatMessageCreateRequest request);

    ChatMessage queryRequest2Entity(ChatMessageQueryRequest request);

    ChatMessage updateEntityFromUpdateRequest(ChatMessageUpdateRequest request, @MappingTarget ChatMessage entity);

    ChatMessageCommonResponse entity2Response(ChatMessage entity);
}