package io.qifan.chatgpt.assistant.infrastructure.mapper;

import io.qifan.chatgpt.assistant.gpt.config.ChatConfig;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        componentModel = MappingConstants.ComponentModel.SPRING
)
public class CustomMapper {
    public Integer GptModel2Int(ChatConfig.GPTModel chatConfigGptModel) {
        return chatConfigGptModel.getCode();
    }

    public ChatConfig.GPTModel int2GPTModel(Integer integer) {
        return ChatConfig.GPTModel.codeOf(integer);
    }

}
