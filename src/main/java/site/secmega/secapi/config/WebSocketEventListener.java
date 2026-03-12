package site.secmega.secapi.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import site.secmega.secapi.feature.user.UserService;
import site.secmega.secapi.feature.user.UserServiceImpl;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketEventListener {

    private final UserService userService;

    @EventListener
    public void handleConnect(SessionConnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String userId = accessor.getFirstNativeHeader("userId");
        log.info("Connected: {}", userId);
        if (userId != null) {
            accessor.getSessionAttributes().put("userId", userId);
            userService.setActive(Long.parseLong(userId));
        }
    }

    @EventListener
    public void handleDisconnect(SessionDisconnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String userId = (String) accessor.getSessionAttributes().get("userId");
        log.info("Disconnected: {}", userId);
        if (userId != null) {
            userService.setInactive(Long.parseLong(userId));
        }
    }
}
