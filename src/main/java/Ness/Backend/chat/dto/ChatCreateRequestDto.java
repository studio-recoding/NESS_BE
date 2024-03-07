package Ness.Backend.chat.dto;

import Ness.Backend.entity.ChatType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatCreateRequestDto {
    @Schema(description = "맴버 고유 인식 넘버", example = "0")
    private Long member_id;

    @Schema(description = "채팅 내용", example = "오늘 내가 공부한 내역을 보여줘.")
    private String text;

    @Schema(description = "발화자 구분", example = "AI")
    private ChatType chatType;
}
