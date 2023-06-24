package io.qifan.chatgpt.assistant.gpt.config.service;

import cn.dev33.satoken.stp.StpUtil;
import io.qifan.chatgpt.assistant.gpt.config.ChatConfig;
import io.qifan.chatgpt.assistant.gpt.config.dto.request.ChatConfigCreateRequest;
import io.qifan.chatgpt.assistant.gpt.config.dto.request.ChatConfigQueryRequest;
import io.qifan.chatgpt.assistant.gpt.config.dto.request.ChatConfigUpdateRequest;
import io.qifan.chatgpt.assistant.gpt.config.dto.response.ChatConfigCommonResponse;
import io.qifan.chatgpt.assistant.gpt.config.mapper.ChatConfigMapper;
import io.qifan.chatgpt.assistant.gpt.config.repository.ChatConfigRepository;
import io.qifan.infrastructure.common.constants.ResultCode;
import io.qifan.infrastructure.common.exception.BusinessException;
import io.qifan.infrastructure.common.model.QueryRequest;
import io.qifan.infrastructure.common.mongo.functional.EntityOperations;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class ChatConfigService {
    private final ChatConfigRepository chatConfigRepository;
    private final ChatConfigMapper chatConfigMapper;
    private final MongoTemplate mongoTemplate;

    public ChatConfigCommonResponse findById(String id) {
        return chatConfigMapper.entity2Response(chatConfigRepository
                                                        .findById(id)
                                                        .orElseThrow(() -> new BusinessException(
                                                                ResultCode.NotFindError)));
    }

    public void updateChatConfig(ChatConfigUpdateRequest request, String id) {
        EntityOperations.doUpdate(chatConfigRepository)
                        .loadById(id)
                        .update(e -> {
                            chatConfigMapper.updateEntityFromUpdateRequest(request, e);
                            e.valid();
                        })
                        .successHook(e -> log.info("更新ChatConfig：{}", e))
                        .execute();
    }

    public String createChatConfig(ChatConfigCreateRequest request) {
        Optional<ChatConfig> chatConfig = EntityOperations.doCreate(chatConfigRepository)
                .create(() -> {
                    Optional.ofNullable(mongoTemplate.findOne(Query.query(Criteria.where("createdBy.id").is(StpUtil.getLoginIdAsString())), ChatConfig.class)).ifPresent(config -> {
                        throw new BusinessException(ResultCode.SaveError, "已存在配置，请刷新页面");
                    });
                    return chatConfigMapper.createRequest2Entity(
                            request);
                })
                                                          .update(ChatConfig::valid)
                                                          .successHook(e -> {
                                                              log.info("创建ChatConfig：{}", e);

                                                          })
                                                          .execute();
        return chatConfig.map(ChatConfig::getId).orElse(null);
    }

    public void validChatConfig(String id) {
        EntityOperations.doUpdate(chatConfigRepository)
                        .loadById(id)
                        .update(ChatConfig::valid)
                        .successHook(e -> log.info("生效ChatConfig：{}", e))
                        .execute();
    }

    public void invalidChatConfig(String id) {
        EntityOperations.doUpdate(chatConfigRepository)
                        .loadById(id)
                        .update(ChatConfig::invalid)
                        .successHook(e -> log.info("失效ChatConfig：{}", e))
                        .execute();
    }

    public Page<ChatConfigCommonResponse> queryChatConfig(QueryRequest<ChatConfigQueryRequest> request) {
        ExampleMatcher matcher = ExampleMatcher.matchingAll();
        Example<ChatConfig> example = Example.of(chatConfigMapper.queryRequest2Entity(request.getQuery()), matcher);
        Page<ChatConfig> page = chatConfigRepository.findAll(example, request.toPageable());
        return page.map(chatConfigMapper::entity2Response);
    }

    public Boolean deleteChatConfig(List<String> ids) {
        chatConfigRepository.deleteAllById(ids);
        return true;
    }

    public ChatConfigCommonResponse getUserChatConfig() {
        return Optional.ofNullable(mongoTemplate.findOne(Query.query(Criteria.where("createdBy.id")
                                                                             .is(StpUtil.getLoginIdAsString())),
                                                         ChatConfig.class))
                       .map(chatConfigMapper::entity2Response)
                       .orElse(null);
    }
}