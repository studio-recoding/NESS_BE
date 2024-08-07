package Ness.Backend.domain.chat.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GetChatDto {
    @Schema(description = "채팅 고유 인식 넘버", example = "0")
    private Long id;

    @Schema(description = "채팅 생성 날짜", example = "2024-01-28 12:34:56")
    private String createdDate;

    @Schema(description = "채팅 내용", example = "오늘 내가 공부한 내역을 보여줘.")
    private String text;

    @Schema(description = "채팅 메타데이터", example = "일정에 대한 JSON 구조")
    private String metadata;

    @Schema(description = "발화자 구분", example = "AI")
    private String chatType;

    @JsonProperty("case")
    private int caseNumber;

    @Builder
    public GetChatDto(Long id, String createdDate, String text, String chatType, int caseNumber, String metadata){
        this.id = id;
        this.createdDate = createdDate;
        this.text = text;
        this.chatType = chatType;
        this.caseNumber = caseNumber;
        this.metadata = metadata;
    }
}
