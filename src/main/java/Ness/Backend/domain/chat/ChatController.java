package Ness.Backend.domain.chat;

import Ness.Backend.domain.chat.dto.ChatCreateRequestDto;
import Ness.Backend.domain.chat.dto.ChatListResponseDto;
import Ness.Backend.domain.member.entity.Member;
import Ness.Backend.global.auth.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "Chat API", description = "사용자의 채팅 내역 관련 API")
public class ChatController {
    private final ChatService chatService;
    @GetMapping("/chat/dev")
    @Operation(summary = "모든 사용자의 채팅 내역", description = "모든 사용자의 채팅 내역을 반환하는 API 입니다.")
    public ResponseEntity<ChatListResponseDto> getAllUserChat(){
        ChatListResponseDto allUserChats = chatService.findAllUserChat();
        return new ResponseEntity<>(allUserChats, HttpStatusCode.valueOf(200));
    }

    @GetMapping("/chat")
    @Operation(summary = "한 사용자의 채팅 내역", description = "한 사용자의 모든 채팅 내역을 반환하는 API 입니다.")
    public ResponseEntity<ChatListResponseDto> getOneUserChat(@AuthUser Member member){
        ChatListResponseDto oneUserChats = chatService.findOneUserChat(member.getId());
        return new ResponseEntity<>(oneUserChats, HttpStatusCode.valueOf(200));
    }

    @PutMapping("/chat/new")
    @Operation(summary = "새로운 채팅 내역", description = "새로운 채팅 내역 저장하는 API 입니다.")
    public ResponseEntity<Long> createChat(@AuthUser Member member, @RequestBody ChatCreateRequestDto chatCreateRequestDto){
        Long userId  = chatService.createNewChat(member.getId(), chatCreateRequestDto);
        return new ResponseEntity<>(userId, HttpStatusCode.valueOf(200));
    }

}
