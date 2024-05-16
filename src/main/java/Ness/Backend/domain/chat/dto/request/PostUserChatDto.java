package Ness.Backend.domain.chat.dto.request;

import Ness.Backend.domain.chat.entity.ChatType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
/* 프론트가 보내주는 유저의 채팅 */
public class PostUserChatDto {
    @Schema(description = "채팅 내용", example = "오늘 내가 공부한 내역을 보여줘.")
    private String text;

    @Schema(description = "발화자 구분", example = "AI/USER/STT")
    private ChatType chatType;
}
