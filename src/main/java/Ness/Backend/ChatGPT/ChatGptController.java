package Ness.Backend.ChatGPT;

import Ness.Backend.Common.CommonResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "ChatGPT API", description = "ChatGPT 통신용 API")
@RequestMapping("/api")
@RequiredArgsConstructor
public class ChatGptController {

    private final ChatGptService chatGptService;


    @PostMapping("/chat")
    public CommonResponse<ChatGptAnswerResponseDto> postChat(@RequestBody QuestionRequestDto questionRequestDto){
        return CommonResponse.onSuccess(HttpStatus.OK.value(), chatGptService.askQuestion(questionRequestDto));
    }
}
