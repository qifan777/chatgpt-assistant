package io.qifan.chatgpt.assistant.gpt.message.service;

import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.service.OpenAiService;
import io.qifan.chatgpt.assistant.gpt.config.ChatConfig;
import io.qifan.chatgpt.assistant.gpt.message.ChatMessage;
import io.qifan.chatgpt.assistant.gpt.message.dto.request.ChatMessageCreateRequest;
import io.qifan.chatgpt.assistant.gpt.message.dto.request.ChatMessageQueryRequest;
import io.qifan.chatgpt.assistant.gpt.message.dto.request.ChatMessageUpdateRequest;
import io.qifan.chatgpt.assistant.gpt.message.dto.response.ChatMessageCommonResponse;
import io.qifan.chatgpt.assistant.gpt.message.mapper.ChatMessageMapper;
import io.qifan.chatgpt.assistant.gpt.message.repository.ChatMessageRepository;
import io.qifan.chatgpt.assistant.gpt.message.service.domainservice.SendMessageService;
import io.qifan.chatgpt.assistant.gpt.session.ChatSession;
import io.qifan.chatgpt.assistant.gpt.session.repository.ChatSessionRepository;
import io.qifan.infrastructure.common.constants.ResultCode;
import io.qifan.infrastructure.common.exception.BusinessException;
import io.qifan.infrastructure.common.model.QueryRequest;
import io.qifan.infrastructure.common.mongo.functional.EntityOperations;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class ChatMessageService {
    private final ChatSessionRepository chatSessionRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatMessageMapper chatMessageMapper;
    private final SendMessageService sendMessageService;

    public ChatMessageCommonResponse findById(String id) {
        return chatMessageMapper.entity2Response(chatMessageRepository
                                                         .findById(id)
                                                         .orElseThrow(() -> new BusinessException(
                                                                 ResultCode.NotFindError)));
    }

    public void updateChatMessage(ChatMessageUpdateRequest request, String id) {
        EntityOperations.doUpdate(chatMessageRepository)
                        .loadById(id)
                        .update(e -> {
                            chatMessageMapper.updateEntityFromUpdateRequest(request, e);
                            e.valid();
                        })
                        .successHook(e -> log.info("更新ChatMessage：{}", e))
                        .execute();
    }

    public String createChatMessage(ChatMessageCreateRequest request) {
        Optional<ChatMessage> chatMessage = EntityOperations.doCreate(chatMessageRepository)
                                                            .create(() -> chatMessageMapper.createRequest2Entity(
                                                                    request))
                                                            .update(ChatMessage::valid)
                                                            .successHook(e -> {
                                                                log.info("创建ChatMessage：{}", e);

                                                            })
                                                            .execute();
        return chatMessage.map(ChatMessage::getId).orElse(null);
    }

    public void validChatMessage(String id) {
        EntityOperations.doUpdate(chatMessageRepository)
                        .loadById(id)
                        .update(ChatMessage::valid)
                        .successHook(e -> log.info("生效ChatMessage：{}", e))
                        .execute();
    }

    public void invalidChatMessage(String id) {
        EntityOperations.doUpdate(chatMessageRepository)
                        .loadById(id)
                        .update(ChatMessage::invalid)
                        .successHook(e -> log.info("失效ChatMessage：{}", e))
                        .execute();
    }

    public Page<ChatMessageCommonResponse> queryChatMessage(QueryRequest<ChatMessageQueryRequest> request) {
        ExampleMatcher matcher = ExampleMatcher.matchingAll();
        Example<ChatMessage> example = Example.of(chatMessageMapper.queryRequest2Entity(request.getQuery()), matcher);
        Page<ChatMessage> page = chatMessageRepository.findAll(example, request.toPageable());
        return page.map(chatMessageMapper::entity2Response);
    }

    public Boolean deleteChatMessage(List<String> ids) {
        chatMessageRepository.deleteAllById(ids);
        return true;
    }

    public void sendMessage(ChatMessageCreateRequest requestMessage, Principal principal) {
        ChatSession chatSession = chatSessionRepository.findById(requestMessage.getSession().getId())
                                                       .orElseThrow(() -> new BusinessException(ResultCode.NotFindError));
        ChatMessage chatMessage = chatMessageMapper.createRequest2Entity(requestMessage);
        ChatConfig chatConfig = sendMessageService.checkConfig(principal);
        OpenAiService openAIService = sendMessageService.createOpenAIService(chatConfig);
        ChatCompletionRequest chatRequest = sendMessageService.createChatRequest(chatMessage, chatConfig);
        sendMessageService.sendMessage(openAIService, chatRequest, chatMessage, chatSession, principal);
    }
}