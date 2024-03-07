package Ness.Backend.chat;

import Ness.Backend.chat.dto.ChatCreateRequestDto;
import Ness.Backend.chat.dto.ChatDto;
import Ness.Backend.chat.dto.ChatListResponseDto;
import Ness.Backend.entity.Chat;
import Ness.Backend.entity.Member;
import Ness.Backend.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatService {
    private final ChatRepository chatRepository;
    private final MemberRepository memberRepository;

    public ChatListResponseDto findAllUserChat(){
        List<Chat> chatList = chatRepository.findAll();

        // ChatListResponseDTO에 매핑
        List<ChatDto> chatDtos = chatList.stream()
                .map(chat -> new ChatDto.ChatDtoBuilder()
                        .id(chat.getId())
                        .createdDate(chat.getCreatedDate())
                        .text(chat.getText())
                        .chatType(chat.getChatType().toString())
                        .build())
                .toList();

        return new ChatListResponseDto(chatDtos);
    }

    public ChatListResponseDto findOneUserChat(Long id){
        List<Chat> chatList = chatRepository.findByMemberId(id);

        // ChatListResponseDTO에 매핑
        List<ChatDto> chatDtos = chatList.stream()
                .map(chat -> new ChatDto.ChatDtoBuilder()
                        .id(chat.getId())
                        .createdDate(chat.getCreatedDate())
                        .text(chat.getText())
                        .chatType(chat.getChatType().toString())
                        .build())
                .toList();
        return new ChatListResponseDto(chatDtos);
    }

    @Transactional
    public Long createNewChat(ChatCreateRequestDto chatCreateRequestDto){
        Member memberEntity = memberRepository.findMemberById(chatCreateRequestDto.getMember_id());
        //새로운 채팅 생성
        Chat newChat = Chat.builder()
                .createdDate(LocalDateTime.now())
                .text(chatCreateRequestDto.getText())
                .chatType(chatCreateRequestDto.getChatType())
                .member(memberEntity)
                .build();
        chatRepository.save(newChat);
        return newChat.getId(); // 저장한 Chat 확인용
    }
}
