package Ness.Backend.chat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatDto {
    @Schema(description = "채팅 고유 인식 넘버", example = "0")
    private Long id;

    @Schema(description = "채팅 생성 날짜", example = "2024-01-28 12:34:56")
    private LocalDateTime createdDate;

    @Schema(description = "채팅 내용", example = "오늘 내가 공부한 내역을 보여줘.")
    private String text;
}
