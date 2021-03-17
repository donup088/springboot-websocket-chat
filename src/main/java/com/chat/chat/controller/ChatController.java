package com.chat.chat.controller;

import com.chat.chat.dto.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.core.MessageSendingOperations;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
@RequestMapping("/chat")
public class ChatController {

    private final MessageSendingOperations messageSendingOperations;

    //WebSocket으로 들어오는 메시지 발행 관리
    //클라이언트에서 /pub/chat/message로 발행 요청이 오면 메시지를 받아 처리
   @MessageMapping("/chat/message")
    public void message(ChatMessage chatMessage) {
        System.out.println("chatMessage = " + chatMessage.getMessage());
        if (ChatMessage.MessageType.ENTER.equals(chatMessage.getType())) {
            chatMessage.setMessage(chatMessage.getSender() + "님이 입장하셨습니다.");
        }
        messageSendingOperations.convertAndSend("/sub/chat/room/" + chatMessage.getRoomId(), chatMessage);
    }
}
