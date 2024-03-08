package Ness.Backend.domain.chat;

import Ness.Backend.domain.chat.dto.ChatCreateRequestDto;
import Ness.Backend.domain.chat.dto.ChatDto;
import Ness.Backend.domain.chat.dto.ChatListResponseDto;
import Ness.Backend.domain.chat.entity.Chat;
import Ness.Backend.domain.member.entity.Member;
import Ness.Backend.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
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
                .map(chat -> ChatDto.builder()
                        .id(chat.getId())
                        .createdDate(chat.getCreatedDate().toString())
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
                .map(chat -> ChatDto.builder()
                        .id(chat.getId())
                        .createdDate(chat.getCreatedDate().toString())
                        .text(chat.getText())
                        .chatType(chat.getChatType().toString())
                        .build())
                .toList();
        return new ChatListResponseDto(chatDtos);
    }

    @Transactional
    public Long createNewChat(Long id, ChatCreateRequestDto chatCreateRequestDto){
        Member memberEntity = memberRepository.findMemberById(id);
        //새로운 채팅 생성
        Chat newChat = Chat.builder()
                .createdDate(LocalDateTime.now(ZoneId.of("Asia/Seoul")).atZone(ZoneId.of("Asia/Seoul")))
                .text(chatCreateRequestDto.getText())
                .chatType(chatCreateRequestDto.getChatType())
                .member(memberEntity)
                .build();
        chatRepository.save(newChat);
        return newChat.getId(); // 저장한 Chat 확인용
    }
}
