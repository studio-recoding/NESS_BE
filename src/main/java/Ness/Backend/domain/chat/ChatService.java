package Ness.Backend.domain.chat;

import Ness.Backend.domain.chat.dto.request.PostUserChatDto;
import Ness.Backend.domain.chat.dto.request.PostFastApiUserChatDto;
import Ness.Backend.domain.chat.dto.response.GetAiChatDto;
import Ness.Backend.domain.chat.dto.response.GetChatDto;
import Ness.Backend.domain.chat.dto.response.GetChatListDto;
import Ness.Backend.domain.chat.dto.response.PostFastApiAiChatDto;
import Ness.Backend.domain.chat.entity.Chat;
import Ness.Backend.domain.member.MemberRepository;
import Ness.Backend.domain.member.entity.Member;
import Ness.Backend.global.fastApi.FastApiChatApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public GetAiChatDto postNewUserChat(Long id, PostUserChatDto postUserChatDto){
        Member memberEntity = memberRepository.findMemberById(id);
        //새로운 채팅 생성
        Chat newChat = Chat.builder()
                .createdDate(LocalDateTime.now(ZoneId.of("Asia/Seoul"))
                        .atZone(ZoneId.of("Asia/Seoul")))
                .text(postUserChatDto.getText())
                .chatType(postUserChatDto.getChatType())
                .member(memberEntity)
                .build();
        chatRepository.save(newChat);

        String answer = postNewAiChat(id, postUserChatDto.getText());
        String parsedAnswer = parseAiChat(answer);

        GetAiChatDto getAiChatDto = GetAiChatDto.builder()
                .answer(parsedAnswer)
                .build();

        return getAiChatDto;
    }

    public String parseAiChat(String text){
        return text.replace("\"", "");
    }

    public String postNewAiChat(Long id, String text){

        PostFastApiUserChatDto Userdto = PostFastApiUserChatDto.builder()
                .message(text)
                .build();

        //Fast API에 전송하기
        PostFastApiAiChatDto AiDto = fastApiChatApi.creatFastApiChat(Userdto);

        return AiDto.getAnswer();
    }
}
