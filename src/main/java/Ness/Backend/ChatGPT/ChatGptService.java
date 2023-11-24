package Ness.Backend.ChatGPT;

import Ness.Backend.Config.ChatGptConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChatGptService {
    private static final RestTemplate restTemplate = new RestTemplate();
    @Value("${open-ai.credentials.api-key}")
    private String OpenAIKey;

    //ChatGPT API 호출하기 위한 Header 생성
    public HttpEntity<ChatGptRequestDto> buildHttpEntity(ChatGptRequestDto chatGptRequestDto) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(ChatGptConfig.MEDIA_TYPE));
        headers.add(ChatGptConfig.AUTHORIZATION, ChatGptConfig.BEARER + OpenAIKey);
        return new HttpEntity<>(chatGptRequestDto, headers);
    }

    //ChatGPT API 호출하기 위한 Body 생성
    public ChatGptResponseDto getResponse(HttpEntity<ChatGptRequestDto> chatGptRequestDtoHttpEntity) {
        ResponseEntity<ChatGptResponseDto> responseEntity = restTemplate.postForEntity(
                ChatGptConfig.URL,
                chatGptRequestDtoHttpEntity,
                ChatGptResponseDto.class);
        return responseEntity.getBody();
    }

    //사용자의 질문 기반으로 답변 생성
    public  ChatGptAnswerResponseDto askQuestion(QuestionRequestDto questionRequestDto){
        List<MessageRequestDto> messages = new ArrayList<>();

        //사용자의 질문 기반으로 message 생성
        messages.add(MessageRequestDto.builder()
                .role(ChatGptConfig.USER_ROLE)
                .content(questionRequestDto.getQuestion())
                .build());
        
        //생성된 message -> Body 생성 -> Header 생성 -> ChatGPT API 호출로 답변 생성
        ChatGptResponseDto chatGptResponseDto =  this.getResponse(this.buildHttpEntity(new ChatGptRequestDto(
                ChatGptConfig.MODEL,
                messages,
                ChatGptConfig.TEMPERATURE
        )));

        return new ChatGptAnswerResponseDto(chatGptResponseDto.getChoiceDtos().get(0).getMessage().getContent());

    }

}
