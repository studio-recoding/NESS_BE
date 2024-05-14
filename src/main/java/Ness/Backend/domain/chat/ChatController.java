package Ness.Backend.domain.chat;

import Ness.Backend.domain.chat.dto.request.PostUserChatDto;
import Ness.Backend.domain.chat.dto.response.GetChatListDto;
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
@RequestMapping("/chat")
public class ChatController {
    private final ChatService chatService;
    @GetMapping("")
    @Operation(summary = "한 사용자의 채팅 내역", description = "한 사용자의 일주일치 채팅 내역을 반환하는 API 입니다.")
    public ResponseEntity<GetChatListDto> getUserChat(@AuthUser Member member){
        GetChatListDto oneUserChats = chatService.getOneWeekUserChat(member.getId());
        return new ResponseEntity<>(oneUserChats, HttpStatusCode.valueOf(200));
    }

    @PostMapping("")
    @Operation(summary = "새로운 채팅으로 AI와 통신", description = "새로운 채팅 내역을 저장하고, 새로운 내역을 포함해서 AI의 응답을 받는 API 입니다.")
    public ResponseEntity<GetChatListDto> postAiChat(@AuthUser Member member, @RequestBody PostUserChatDto postUserChatDto) {
        GetChatListDto oneUserChats = chatService.postNewUserChat(member.getId(), postUserChatDto);
        return new ResponseEntity<>(oneUserChats, HttpStatusCode.valueOf(200));
    }
}
