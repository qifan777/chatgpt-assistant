package io.qifan.chatgpt.assistant.user.service;

import cn.dev33.satoken.secure.BCrypt;
import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import io.qifan.chatgpt.assistant.user.User;
import io.qifan.chatgpt.assistant.user.dto.request.UserCreateRequest;
import io.qifan.chatgpt.assistant.user.dto.request.UserQueryRequest;
import io.qifan.chatgpt.assistant.user.dto.request.UserUpdateRequest;
import io.qifan.chatgpt.assistant.user.dto.response.UserCommonResponse;
import io.qifan.chatgpt.assistant.user.mapper.UserMapper;
import io.qifan.chatgpt.assistant.user.repository.UserRepository;
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
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final MongoTemplate mongoTemplate;

    public UserCommonResponse findById(String id) {
        return userMapper.entity2Response(userRepository
                                                  .findById(id)
                                                  .orElseThrow(() -> new BusinessException(
                                                          ResultCode.NotFindError)));
    }

    public void updateUser(UserUpdateRequest request, String id) {
        EntityOperations.doUpdate(userRepository)
                        .loadById(id)
                        .update(e -> {
                            userMapper.updateEntityFromUpdateRequest(request, e);
                            e.valid();
                        })
                        .successHook(e -> log.info("更新User：{}", e))
                        .execute();
    }

    public String createUser(UserCreateRequest request) {
        Optional<User> user = EntityOperations.doCreate(userRepository)
                                              .create(() -> userMapper.createRequest2Entity(
                                                      request))
                                              .update(User::valid)
                                              .successHook(e -> {
                                                  log.info("创建User：{}", e);

                                              })
                                              .execute();
        return user.map(User::getId).orElse(null);
    }

    public void validUser(String id) {
        EntityOperations.doUpdate(userRepository)
                        .loadById(id)
                        .update(User::valid)
                        .successHook(e -> log.info("生效User：{}", e))
                        .execute();
    }

    public void invalidUser(String id) {
        EntityOperations.doUpdate(userRepository)
                        .loadById(id)
                        .update(User::invalid)
                        .successHook(e -> log.info("失效User：{}", e))
                        .execute();
    }

    public Page<UserCommonResponse> queryUser(QueryRequest<UserQueryRequest> request) {
        ExampleMatcher matcher = ExampleMatcher.matchingAll();
        Example<User> example = Example.of(userMapper.queryRequest2Entity(request.getQuery()), matcher);
        Page<User> page = userRepository.findAll(example, request.toPageable());
        return page.map(userMapper::entity2Response);
    }

    public Boolean deleteUser(List<String> ids) {
        userRepository.deleteAllById(ids);
        return true;
    }

    public SaTokenInfo login(UserCreateRequest request) {
        // 用户可能存在
        Optional<User> optionalUser = Optional.ofNullable(mongoTemplate.findOne(Query.query(Criteria.where("username")
                                                                                                    .is(request.getUsername())),
                                                                                User.class));
        // 现在得到的user一定存在，因为不存在已经走了这个创建的流程。
        User user = optionalUser.orElseGet(() -> {
            User request2Entity = userMapper.createRequest2Entity(request);
            request2Entity.setPassword(BCrypt.hashpw(request.getPassword()));
            return userRepository.save(request2Entity);
        });
        if (!BCrypt.checkpw(request.getPassword(), user.getPassword())) {
            throw new BusinessException(ResultCode.ValidateError, "密码校验失败");
        }
        StpUtil.login(user.getId());
        return StpUtil.getTokenInfo();
    }


    public UserCommonResponse getUserInfo() {
        return userRepository.findById(StpUtil.getLoginIdAsString())
                             .map(userMapper::entity2Response)
                             .orElseThrow(() -> new BusinessException(ResultCode.NotFindError,
                                                                      "用户不存在"));
    }
}