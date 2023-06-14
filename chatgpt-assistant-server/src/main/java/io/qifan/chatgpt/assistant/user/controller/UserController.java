package io.qifan.chatgpt.assistant.user.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.SaTokenInfo;
import io.qifan.chatgpt.assistant.user.dto.request.UserCreateRequest;
import io.qifan.chatgpt.assistant.user.dto.request.UserQueryRequest;
import io.qifan.chatgpt.assistant.user.dto.request.UserUpdateRequest;
import io.qifan.chatgpt.assistant.user.dto.response.UserCommonResponse;
import io.qifan.chatgpt.assistant.user.service.UserService;
import io.qifan.infrastructure.common.model.QueryRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("user")
public class UserController {
    private final UserService userService;

    @SaCheckLogin
    @GetMapping("{id}")
    public UserCommonResponse findById(@PathVariable String id) {
        return userService.findById(id);
    }

    @PostMapping("create")
    public String createUser(@RequestBody UserCreateRequest createRequest) {
        return userService.createUser(createRequest);
    }

    @PostMapping("{id}/update")
    public Boolean updateUser(@RequestBody UserUpdateRequest updateRequest, @PathVariable String id) {
        userService.updateUser(updateRequest, id);
        return true;
    }

    @PostMapping("{id}/valid")
    public Boolean validUser(@PathVariable String id) {
        userService.validUser(id);
        return true;
    }

    @PostMapping("{id}/invalid")
    public Boolean invalidUser(@PathVariable String id) {
        userService.invalidUser(id);
        return true;
    }

    @PostMapping("query")
    public Page<UserCommonResponse> queryUser(
            @RequestBody QueryRequest<UserQueryRequest> queryRequest) {
        return userService.queryUser(queryRequest);
    }

    @PostMapping("delete")
    public Boolean deleteUser(@RequestBody List<String> ids) {
        return userService.deleteUser(ids);
    }

    @PostMapping("login")
    public SaTokenInfo login(@RequestBody UserCreateRequest request) {
        return userService.login(request);
    }

    @GetMapping("info")
    @SaCheckLogin
    public UserCommonResponse userInfo() {
        return userService.getUserInfo();
    }
}