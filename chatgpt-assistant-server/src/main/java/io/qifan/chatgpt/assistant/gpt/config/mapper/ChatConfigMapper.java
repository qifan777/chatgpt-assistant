package io.qifan.chatgpt.assistant.gpt.config.mapper;

import io.qifan.chatgpt.assistant.gpt.config.ChatConfig;
import io.qifan.chatgpt.assistant.gpt.config.dto.request.ChatConfigCreateRequest;
import io.qifan.chatgpt.assistant.gpt.config.dto.request.ChatConfigQueryRequest;
import io.qifan.chatgpt.assistant.gpt.config.dto.request.ChatConfigUpdateRequest;
import io.qifan.chatgpt.assistant.gpt.config.dto.response.ChatConfigCommonResponse;
import io.qifan.chatgpt.assistant.infrastructure.mapper.CustomMapper;
import org.mapstruct.*;

@Mapper(
        uses = {CustomMapper.class},
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING
)
public interface ChatConfigMapper {

    ChatConfig createRequest2Entity(ChatConfigCreateRequest request);

    ChatConfig queryRequest2Entity(ChatConfigQueryRequest request);

    @Mapping(target = "apiKey", conditionQualifiedByName = "ApiKeyCheck")
    ChatConfig updateEntityFromUpdateRequest(ChatConfigUpdateRequest request, @MappingTarget ChatConfig entity);

    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "apiKey", qualifiedByName = "ApiKeyMap")
    ChatConfigCommonResponse entity2Response(ChatConfig entity);

    @Condition
    @Named("ApiKeyCheck")
    default boolean apiKeyCheck(String apiKey) {
        return !apiKey.equals("**********");
    }

    @Named("ApiKeyMap")
    default String apiKeyMap(String apiKey) {
        return apiKey == null ? null : "**********";
    }
}