package io.qifan.chatgpt.assistant.gpt.session.service;

import cn.dev33.satoken.stp.StpUtil;
import io.qifan.chatgpt.assistant.gpt.session.ChatSession;
import io.qifan.chatgpt.assistant.gpt.session.dto.request.ChatSessionCreateRequest;
import io.qifan.chatgpt.assistant.gpt.session.dto.request.ChatSessionQueryRequest;
import io.qifan.chatgpt.assistant.gpt.session.dto.request.ChatSessionUpdateRequest;
import io.qifan.chatgpt.assistant.gpt.session.dto.response.ChatSessionCommonResponse;
import io.qifan.chatgpt.assistant.gpt.session.mapper.ChatSessionMapper;
import io.qifan.chatgpt.assistant.gpt.session.repository.ChatSessionRepository;
import io.qifan.chatgpt.assistant.user.User;
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

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class ChatSessionService {
    private final ChatSessionRepository chatSessionRepository;
    private final ChatSessionMapper chatSessionMapper;

    public ChatSessionCommonResponse findById(String id) {
        return chatSessionMapper.entity2Response(chatSessionRepository
                                                         .findById(id)
                                                         .orElseThrow(() -> new BusinessException(
                                                                 ResultCode.NotFindError)));
    }

    public void updateChatSession(ChatSessionUpdateRequest request, String id) {
        EntityOperations.doUpdate(chatSessionRepository)
                        .loadById(id)
                        .update(e -> {
                            chatSessionMapper.updateEntityFromUpdateRequest(request, e);
                            e.valid();
                        })
                        .successHook(e -> log.info("更新ChatSession：{}", e))
                        .execute();
    }

    public String createChatSession(ChatSessionCreateRequest request) {
        Optional<ChatSession> chatSession = EntityOperations.doCreate(chatSessionRepository)
                                                            .create(() -> {
                                                                ChatSession request2Entity = chatSessionMapper.createRequest2Entity(
                                                                        request);
                                                                request2Entity.setTopic("新的聊天");
                                                                request2Entity.setStatistic(new ChatSession.Statistic(0,
                                                                                                                      0));
                                                                return request2Entity;
                                                            })
                                                            .update(ChatSession::valid)
                                                            .successHook(e -> {
                                                                log.info("创建ChatSession：{}", e);

                                                            })
                                                            .execute();
        return chatSession.map(ChatSession::getId).orElse(null);
    }

    public void validChatSession(String id) {
        EntityOperations.doUpdate(chatSessionRepository)
                        .loadById(id)
                        .update(ChatSession::valid)
                        .successHook(e -> log.info("生效ChatSession：{}", e))
                        .execute();
    }

    public void invalidChatSession(String id) {
        EntityOperations.doUpdate(chatSessionRepository)
                        .loadById(id)
                        .update(ChatSession::invalid)
                        .successHook(e -> log.info("失效ChatSession：{}", e))
                        .execute();
    }

    public Page<ChatSessionCommonResponse> queryChatSession(QueryRequest<ChatSessionQueryRequest> request) {
        ExampleMatcher matcher = ExampleMatcher.matchingAll();
        ChatSession chatSession = chatSessionMapper.queryRequest2Entity(request.getQuery());
        User user = new User();
        user.setId(StpUtil.getLoginIdAsString());
        chatSession.setCreatedBy(user);
        Example<ChatSession> example = Example.of(chatSession, matcher);
        Page<ChatSession> page = chatSessionRepository.findAll(example, request.toPageable());
        return page.map(chatSessionMapper::entity2Response);
    }

    public Boolean deleteChatSession(List<String> ids) {
        chatSessionRepository.deleteAllById(ids);
        return true;
    }
}