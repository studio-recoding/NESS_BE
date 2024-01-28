package Ness.Backend.chat;

import Ness.Backend.domain.Chat;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Chat API", description = "사용자의 채팅 내역 관련 API")
public class ChatController {
    private final ChatService chatService;
    @GetMapping("/chat")
    @Operation(summary = "모든 사용자의 채팅 내역", description = "모든 사용자의 채팅 내역을 반환하는 API 입니다.")
    public ResponseEntity<List<Chat>> getAllUserChat(){
        List<Chat> allUserChats = chatService.findAllUserChat();
        return new ResponseEntity<List<Chat>>(allUserChats, HttpStatusCode.valueOf(200));
    }

    @GetMapping("/chat/user")
    @Operation(summary = "모든 사용자의 채팅 내역", description = "모든 사용자의 채팅 내역을 반환하는 API 입니다.")
    public ResponseEntity<List<Chat>> getOneUserChat(@RequestBody ChatRequestDto chatRequestDto){
        List<Chat> oneUserChats = chatService.findOneUserChat(chatRequestDto.getMember_id());
        return new ResponseEntity<List<Chat>>(oneUserChats, HttpStatusCode.valueOf(200));
    }

}
