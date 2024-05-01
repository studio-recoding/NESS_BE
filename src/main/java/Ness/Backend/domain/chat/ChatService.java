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

    public ZonedDateTime createdZonedDate(){
        return LocalDateTime
                .now(ZoneId.of("Asia/Seoul"))
                .atZone(ZoneId.of("Asia/Seoul"));
    }

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
        String parsedAnswer = parseAiChat(AiDto.getAnswer());

        //AI 챗 답변 저장
        Chat newAiChat = Chat.builder()
                .createdDate(LocalDateTime.now(ZoneId.of("Asia/Seoul"))
                        .atZone(ZoneId.of("Asia/Seoul")))
                .text(parsedAnswer)
                .chatType(ChatType.AI)
                .caseNumber(AiDto.getCaseNumber()) //AI는 받아온 값으로 저장
                .member(memberEntity)
                .build();

        chatRepository.save(newAiChat);
        return getOneWeekUserChat(id);
    }

    /* ChatGPT가 답변의 앞뒤에 \를 포함시키므로, 제거 필요 */
    public String parseAiChat(String text){
        return text.replace("\"", "");
    }

    public PostFastApiAiChatDto postNewAiChat(Long id, String text){

        PostFastApiUserChatDto userDto = PostFastApiUserChatDto.builder()
                .persona("default")
                .message(text)
                .build();

        //Fast API에 전송하기
        PostFastApiAiChatDto aiDto = fastApiChatApi.creatFastApiChat(userDto);

        return aiDto;
    }
}
