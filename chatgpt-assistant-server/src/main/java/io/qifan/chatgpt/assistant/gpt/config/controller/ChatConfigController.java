package io.qifan.chatgpt.assistant.gpt.config.controller;

import io.qifan.chatgpt.assistant.gpt.config.dto.request.ChatConfigCreateRequest;
import io.qifan.chatgpt.assistant.gpt.config.dto.request.ChatConfigQueryRequest;
import io.qifan.chatgpt.assistant.gpt.config.dto.request.ChatConfigUpdateRequest;
import io.qifan.chatgpt.assistant.gpt.config.dto.response.ChatConfigCommonResponse;
import io.qifan.chatgpt.assistant.gpt.config.service.ChatConfigService;
import io.qifan.infrastructure.common.model.QueryRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("chatConfig")
public class ChatConfigController {
    private final ChatConfigService chatConfigService;

    @GetMapping("{id}")
    public ChatConfigCommonResponse findById(@PathVariable String id) {
        return chatConfigService.findById(id);
    }

    @PostMapping("create")
    public String createChatConfig(@RequestBody @Valid ChatConfigCreateRequest createRequest) {
        return chatConfigService.createChatConfig(createRequest);
    }

    @PostMapping("{id}/update")
    public Boolean updateChatConfig(@RequestBody @Valid ChatConfigUpdateRequest updateRequest,
                                    @PathVariable String id) {
        chatConfigService.updateChatConfig(updateRequest, id);
        return true;
    }

    @PostMapping("{id}/valid")
    public Boolean validChatConfig(@PathVariable String id) {
        chatConfigService.validChatConfig(id);
        return true;
    }

    @PostMapping("{id}/invalid")
    public Boolean invalidChatConfig(@PathVariable String id) {
        chatConfigService.invalidChatConfig(id);
        return true;
    }

    @PostMapping("query")
    public Page<ChatConfigCommonResponse> queryChatConfig(
            @RequestBody QueryRequest<ChatConfigQueryRequest> queryRequest) {
        return chatConfigService.queryChatConfig(queryRequest);
    }

    @PostMapping("delete")
    public Boolean deleteChatConfig(@RequestBody List<String> ids) {
        return chatConfigService.deleteChatConfig(ids);
    }

    @GetMapping("user")
    public ChatConfigCommonResponse getUserChatConfig() {
        return chatConfigService.getUserChatConfig();
    }
}