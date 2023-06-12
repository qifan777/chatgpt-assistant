package io.qifan.chatgpt.assistant.infrastructure.websocket;

import cn.dev33.satoken.stp.StpUtil;
import com.sun.security.auth.UserPrincipal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;

@Slf4j
public class UserHandShakeHandler extends DefaultHandshakeHandler {
    @Override
    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler,
                                      Map<String, Object> attributes) {

        log.info("当前登录用户id：{}", StpUtil.getLoginIdAsString());
        return new UserPrincipal(StpUtil.getLoginIdAsString());
    }
}
