package Ness.Backend.domain.chat;

import Ness.Backend.domain.chat.dto.request.PostChatDto;
import Ness.Backend.domain.chat.dto.request.PostFastApiUserChatDto;
import Ness.Backend.domain.chat.dto.response.GetChatDto;
import Ness.Backend.domain.chat.dto.response.GetChatListDto;
import Ness.Backend.domain.chat.entity.Chat;
import Ness.Backend.domain.member.MemberRepository;
import Ness.Backend.domain.member.entity.Member;
import Ness.Backend.global.fastApi.FastApiChatApi;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ChatService {
    private final ChatRepository chatRepository;
    private final MemberRepository memberRepository;
    private final FastApiChatApi fastApiChatApi;

    public GetChatListDto getOneWeekUserChat(Long id){
        List<Chat> chatList = chatRepository.findOneWeekUserChatsByMember_Id(id);

        // ChatListResponseDTO에 매핑
        List<GetChatDto> getChatDtos = chatList.stream()
                .map(chat -> GetChatDto.builder()
                        .id(chat.getId())
                        .createdDate(chat.getCreatedDate().toString())
                        .text(chat.getText())
                        .chatType(chat.getChatType().toString())
                        .build())
                .toList();
        return new GetChatListDto(getChatDtos);
    }

    @Transactional
    public Long postNewUserChat(Long id, PostChatDto postChatDto){
        Member memberEntity = memberRepository.findMemberById(id);
        //새로운 채팅 생성
        Chat newChat = Chat.builder()
                .createdDate(LocalDateTime.now(ZoneId.of("Asia/Seoul"))
                        .atZone(ZoneId.of("Asia/Seoul")))
                .text(postChatDto.getText())
                .chatType(postChatDto.getChatType())
                .member(memberEntity)
                .build();
        chatRepository.save(newChat);

        postNewAiChat(id, postChatDto.getText());

        return newChat.getId(); // 저장한 Chat 확인용
    }

    public void postNewAiChat(Long id, String text){

        PostFastApiUserChatDto dto = PostFastApiUserChatDto.builder()
                .member_id(id)
                .message(text)
                .build();

        //Fast API에 전송하기
        ResponseEntity<JsonNode> responseNode = fastApiChatApi.creatFastApiChat(dto);
        if (responseNode.getStatusCode() == HttpStatusCode.valueOf(200)) {
            log.info("Succeed to get Response from LLM");
        } else {
            log.error("Failed to get Response from LLM");
        }
    }
}
