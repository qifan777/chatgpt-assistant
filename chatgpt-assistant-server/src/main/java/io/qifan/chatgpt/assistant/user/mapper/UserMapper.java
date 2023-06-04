package io.qifan.chatgpt.assistant.user.mapper;

import io.qifan.chatgpt.assistant.infrastructure.mapper.CustomMapper;
import io.qifan.chatgpt.assistant.user.User;
import io.qifan.chatgpt.assistant.user.dto.request.UserCreateRequest;
import io.qifan.chatgpt.assistant.user.dto.request.UserQueryRequest;
import io.qifan.chatgpt.assistant.user.dto.request.UserUpdateRequest;
import io.qifan.chatgpt.assistant.user.dto.response.UserCommonResponse;
import org.mapstruct.*;

@Mapper(
        uses = {CustomMapper.class},
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING
)
public interface UserMapper {

    User createRequest2Entity(UserCreateRequest request);

    User queryRequest2Entity(UserQueryRequest request);

    User updateEntityFromUpdateRequest(UserUpdateRequest request, @MappingTarget User entity);

    UserCommonResponse entity2Response(User entity);
}