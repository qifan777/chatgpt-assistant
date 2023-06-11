package io.qifan.chatgpt.assistant.gpt.session.controller;

import io.qifan.chatgpt.assistant.gpt.session.dto.request.ChatSessionCreateRequest;
import io.qifan.chatgpt.assistant.gpt.session.dto.request.ChatSessionQueryRequest;
import io.qifan.chatgpt.assistant.gpt.session.dto.request.ChatSessionUpdateRequest;
import io.qifan.chatgpt.assistant.gpt.session.dto.response.ChatSessionCommonResponse;
import io.qifan.chatgpt.assistant.gpt.session.service.ChatSessionService;
import io.qifan.infrastructure.common.model.QueryRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("chatSession")
public class ChatSessionController {
    private final ChatSessionService chatSessionService;

    @GetMapping("{id}")
    public ChatSessionCommonResponse findById(@PathVariable String id) {
        return chatSessionService.findById(id);
    }

    @PostMapping("create")
    public String createChatSession(@RequestBody ChatSessionCreateRequest createRequest) {
        return chatSessionService.createChatSession(createRequest);
    }

    @PostMapping("{id}/update")
    public Boolean updateChatSession(@RequestBody ChatSessionUpdateRequest updateRequest, @PathVariable String id) {
        chatSessionService.updateChatSession(updateRequest, id);
        return true;
    }

    @PostMapping("{id}/valid")
    public Boolean validChatSession(@PathVariable String id) {
        chatSessionService.validChatSession(id);
        return true;
    }

    @PostMapping("{id}/invalid")
    public Boolean invalidChatSession(@PathVariable String id) {
        chatSessionService.invalidChatSession(id);
        return true;
    }

    @PostMapping("query")
    public Page<ChatSessionCommonResponse> queryChatSession(
            @RequestBody QueryRequest<ChatSessionQueryRequest> queryRequest) {
        return chatSessionService.queryChatSession(queryRequest);
    }

    @PostMapping("delete")
    public Boolean deleteChatSession(@RequestBody List<String> ids) {
        return chatSessionService.deleteChatSession(ids);
    }
}