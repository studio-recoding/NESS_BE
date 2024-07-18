package Ness.Backend.domain.chat;

import Ness.Backend.domain.chat.dto.request.PostFastApiUserChatDto;
import Ness.Backend.domain.chat.dto.request.PostUserChatDto;
import Ness.Backend.domain.chat.dto.response.GetChatDto;
import Ness.Backend.domain.chat.dto.response.GetChatListDto;
import Ness.Backend.domain.chat.dto.response.PostFastApiAiChatDto;
import Ness.Backend.domain.chat.entity.Chat;
import Ness.Backend.domain.chat.entity.ChatType;
import Ness.Backend.domain.member.MemberRepository;
import Ness.Backend.domain.member.entity.Member;
import Ness.Backend.domain.profile.ProfileRepository;
import Ness.Backend.domain.profile.entity.PersonaType;
import Ness.Backend.domain.profile.entity.Profile;
import Ness.Backend.global.fastApi.FastApiChatApi;
import Ness.Backend.global.time.GlobalTime;
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
@Transactional
public class ChatService {
    private final ChatRepository chatRepository;
    private final MemberRepository memberRepository;
    private final ProfileRepository profileRepository;
    private final FastApiChatApi fastApiChatApi;
    private final GlobalTime globalTime;

    /* 새로운 채팅 생성 및 RDB에 저장 */
    public void createNewChat(String text, ChatType chatType, int caseNumber, Member member){
        Chat chat = Chat.builder()
                .createdDate(globalTime.createdZonedDate())
                .text(text)
                .chatType(chatType)
                .caseNumber(caseNumber)
                .member(member)
                .build();

        chatRepository.save(chat);
    }

    /* 새로운 채팅 생성 및 RDB에 저장 */
    public void createNewChatWithMetaData(String text, ChatType chatType, int caseNumber, Member member, String metadata){
        Chat chat = Chat.builder()
                .createdDate(globalTime.createdZonedDate())
                .text(text)
                .chatType(chatType)
                .caseNumber(caseNumber)
                .member(member)
                .metadata(metadata)
                .build();

        chatRepository.save(chat);
    }

    /* 일주일 치 채팅 데이터 가져오기*/
    public GetChatListDto getOneWeekUserChat(Long memberId){
        List<Chat> chatList = chatRepository.findOneWeekUserChatsByMember_Id(memberId);

        // ChatListResponseDTO에 매핑
        List<GetChatDto> getChatDtos = chatList.stream()
                .map(chat -> GetChatDto.builder()
                        .id(chat.getId())
                        .createdDate(chat.getCreatedDate().toString())
                        .text(chat.getText())
                        .caseNumber(chat.getCaseNumber())
                        .chatType(chat.getChatType().toString())
                        .metadata(chat.getMetadata())
                        .build())
                .toList();
        return new GetChatListDto(getChatDtos);
    }

    /* 유저의 채팅을 AI에 전송한 후, 답변 및 유저 채팅을 RDB에 저장 */
    public GetChatListDto postNewUserChat(Long memberId, PostUserChatDto postUserChatDto){
        Member memberEntity = memberRepository.findMemberById(memberId);
        Profile profileEntity = profileRepository.findProfileByMember_Id(memberId);
        //새로운 유저 채팅 저장
        Chat newUserChat = Chat.builder()
                .createdDate(globalTime.createdZonedDate())
                .text(postUserChatDto.getText())
                .chatType(postUserChatDto.getChatType())
                .caseNumber(0) //유저는 디폴트로 case 0
                .member(memberEntity)
                .build();

        chatRepository.save(newUserChat);

        PostFastApiAiChatDto AiDto = postNewAiChat(memberId, postUserChatDto.getText(), postUserChatDto.getChatType(), profileEntity.getPersonaType());

        //AI 챗 답변 저장
        Chat newAiChat = Chat.builder()
                .createdDate(LocalDateTime.now(ZoneId.of("Asia/Seoul"))
                        .atZone(ZoneId.of("Asia/Seoul")))
                .text(parseAiChat(AiDto.getAnswer()))
                .chatType(ChatType.AI)
                .caseNumber(AiDto.getCaseNumber()) //AI는 받아온 값으로 저장
                .metadata(AiDto.getMetadata())
                .member(memberEntity)
                .build();

        chatRepository.save(newAiChat);
        return getOneWeekUserChat(memberId);
    }

    /* AI에 채팅 전송하는 로직 */
    public PostFastApiAiChatDto postNewAiChat(Long memberId, String text, ChatType chatType, PersonaType personaType){
        String persona = "default";
        if (personaType == PersonaType.HARDNESS){
            persona = "hard";
        }
        if (personaType == PersonaType.CALMNESS){
            persona = "calm";
        }

        PostFastApiUserChatDto userDto = PostFastApiUserChatDto.builder()
                .persona(persona)
                .chatType(chatType) // 유저가 키보드로 친 채팅인지, 아니면 STT를 썼는지 구분
                .message(text)
                .member_id(memberId)
                .build();

        //Fast API에 전송하기
        PostFastApiAiChatDto aiDto = fastApiChatApi.creatFastApiChat(userDto);

        return aiDto;
    }

    public String parseAiChat(String text){
        // AI에서 이 접두사를 붙여서 반환하는 경우가 많이 발생함
        return text.replace("NESS: ", "");
    }
}
