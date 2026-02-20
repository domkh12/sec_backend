package site.secmega.secapi.feature.message;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import site.secmega.secapi.feature.message.dto.MessageRequest;

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class MessageController {

    private final SimpMessagingTemplate messagingTemplate;
    @MessageMapping("/chat/{to}")
    public void sendMessage(@DestinationVariable String to, MessageRequest message) {
        log.info(message.toString() + " to " + to);
        messagingTemplate.convertAndSend("/topic/messages/" + to, List.of(message));
    }

}
