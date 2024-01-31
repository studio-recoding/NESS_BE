package Ness.Backend.chat;

import Ness.Backend.domain.Chat;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatService {
    private final ChatRepository chatRepository;

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

}
