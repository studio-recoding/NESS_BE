package Ness.Backend.chat;

import Ness.Backend.domain.Chat;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatService {
    private final ChatRepository chatRepository;

    public List<Chat> findAllUserChat(){
        return chatRepository.findAll();
    }

    public List<Chat> findOneUserChat(Long id){
        return chatRepository.findByMemberId(id);
    }

}
