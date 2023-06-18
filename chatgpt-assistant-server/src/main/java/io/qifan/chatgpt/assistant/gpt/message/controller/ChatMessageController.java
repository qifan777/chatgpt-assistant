package io.qifan.chatgpt.assistant.gpt.message.controller;

import io.qifan.chatgpt.assistant.gpt.message.dto.request.ChatMessageCreateRequest;
import io.qifan.chatgpt.assistant.gpt.message.dto.request.ChatMessageQueryRequest;
import io.qifan.chatgpt.assistant.gpt.message.dto.request.ChatMessageUpdateRequest;
import io.qifan.chatgpt.assistant.gpt.message.dto.response.ChatMessageCommonResponse;
import io.qifan.chatgpt.assistant.gpt.message.service.ChatMessageService;
import io.qifan.infrastructure.common.model.QueryRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("chatMessage")
public class ChatMessageController {
    private final ChatMessageService chatMessageService;

    @GetMapping("{id}")
    public ChatMessageCommonResponse findById(@PathVariable String id) {
        return chatMessageService.findById(id);
    }

    @PostMapping("create")
    public String createChatMessage(@RequestBody ChatMessageCreateRequest createRequest) {
        return chatMessageService.createChatMessage(createRequest);
    }

    @PostMapping("{id}/update")
    public Boolean updateChatMessage(@RequestBody ChatMessageUpdateRequest updateRequest, @PathVariable String id) {
        chatMessageService.updateChatMessage(updateRequest, id);
        return true;
    }

    @PostMapping("{id}/valid")
    public Boolean validChatMessage(@PathVariable String id) {
        chatMessageService.validChatMessage(id);
        return true;
    }

    @PostMapping("{id}/invalid")
    public Boolean invalidChatMessage(@PathVariable String id) {
        chatMessageService.invalidChatMessage(id);
        return true;
    }

    @PostMapping("query")
    public Page<ChatMessageCommonResponse> queryChatMessage(
            @RequestBody QueryRequest<ChatMessageQueryRequest> queryRequest) {
        return chatMessageService.queryChatMessage(queryRequest);
    }

    @PostMapping("delete")
    public Boolean deleteChatMessage(@RequestBody List<String> ids) {
        return chatMessageService.deleteChatMessage(ids);
    }
}