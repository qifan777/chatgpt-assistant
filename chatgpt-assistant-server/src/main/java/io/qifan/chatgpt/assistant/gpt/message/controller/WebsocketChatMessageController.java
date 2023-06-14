package io.qifan.chatgpt.assistant.gpt.message.controller;

import io.qifan.chatgpt.assistant.gpt.message.dto.request.ChatMessageCreateRequest;
import io.qifan.chatgpt.assistant.gpt.message.service.ChatMessageService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@AllArgsConstructor
@Slf4j
public class WebsocketChatMessageController {
    private final ChatMessageService chatMessageService;

    @MessageMapping("/chatMessage/send")
    public void chat(@Payload ChatMessageCreateRequest requestMessage, Principal principal) {
        chatMessageService.sendMessage(requestMessage, principal);
    }
}
