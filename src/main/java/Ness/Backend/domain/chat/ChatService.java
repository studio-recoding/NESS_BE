package Ness.Backend.domain.chat;

import Ness.Backend.domain.chat.dto.request.PostUserChatDto;
import Ness.Backend.domain.chat.dto.request.PostFastApiUserChatDto;
import Ness.Backend.domain.chat.dto.response.GetChatDto;
import Ness.Backend.domain.chat.dto.response.GetChatListDto;
import Ness.Backend.domain.chat.dto.response.PostFastApiAiChatDto;
import Ness.Backend.domain.chat.entity.Chat;
import Ness.Backend.domain.chat.entity.ChatType;
import Ness.Backend.domain.member.MemberRepository;
import Ness.Backend.domain.member.entity.Member;
import Ness.Backend.global.fastApi.FastApiChatApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ChatService {
    private final ChatRepository chatRepository;
    private final MemberRepository memberRepository;
    private final FastApiChatApi fastApiChatApi;

    /* 새로운 채팅 생성 및 RDB에 저장 */
    public void createNewChat(Long memberId, String text, ChatType chatType, int caseNumber, Member member){
        Chat chat = Chat.builder()
                .createdDate(createdZonedDate())
                .text(text)
                .chatType(chatType)
                .caseNumber(caseNumber)
                .member(member)
                .build();

        chatRepository.save(chat);
    }

    /* 일주일 치 채팅 데이터 가져오기*/
    public GetChatListDto getOneWeekUserChat(Long id){
        List<Chat> chatList = chatRepository.findOneWeekUserChatsByMember_Id(id);

        // ChatListResponseDTO에 매핑
        List<GetChatDto> getChatDtos = chatList.stream()
                .map(chat -> GetChatDto.builder()
                        .id(chat.getId())
                        .createdDate(chat.getCreatedDate().toString())
                        .text(chat.getText())
                        .caseNumber(chat.getCaseNumber())
                        .chatType(chat.getChatType().toString())
                        .build())
                .toList();
        return new GetChatListDto(getChatDtos);
    }

    /* 유저의 채팅을 AI에 전송한 후, 답변 및 유저 채팅을 RDB에 저장 */
    public GetChatListDto postNewUserChat(Long id, PostUserChatDto postUserChatDto){
        Member memberEntity = memberRepository.findMemberById(id);
        //새로운 유저 채팅 저장
        Chat newUserChat = Chat.builder()
                .createdDate(createdZonedDate())
                .text(postUserChatDto.getText())
                .chatType(postUserChatDto.getChatType())
                .caseNumber(0) //유저는 디폴트로 case
                .member(memberEntity)
                .build();

        chatRepository.save(newUserChat);

        PostFastApiAiChatDto AiDto = postNewAiChat(id, postUserChatDto.getText());

        //AI 챗 답변 저장
        Chat newAiChat = Chat.builder()
                .createdDate(LocalDateTime.now(ZoneId.of("Asia/Seoul"))
                        .atZone(ZoneId.of("Asia/Seoul")))
                .text(AiDto.getAnswer())
                .chatType(ChatType.AI)
                .caseNumber(AiDto.getCaseNumber()) //AI는 받아온 값으로 저장
                .member(memberEntity)
                .build();

        chatRepository.save(newAiChat);
        return getOneWeekUserChat(id);
    }

    /* AI에 채팅 전송하는 로직 */
    public PostFastApiAiChatDto postNewAiChat(Long id, String text){

        PostFastApiUserChatDto userDto = PostFastApiUserChatDto.builder()
                .persona("default")
                .message(text)
                .build();

        //Fast API에 전송하기
        PostFastApiAiChatDto aiDto = fastApiChatApi.creatFastApiChat(userDto);

        return aiDto;
    }

    public ZonedDateTime createdZonedDate(){
        return LocalDateTime
                .now(ZoneId.of("Asia/Seoul"))
                .atZone(ZoneId.of("Asia/Seoul"));
    }
}
