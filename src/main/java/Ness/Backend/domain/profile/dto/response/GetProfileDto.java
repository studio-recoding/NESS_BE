package Ness.Backend.domain.profile.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProfileResponseDto {

    @Schema(description = "프로필 고유 아이디", example = "0")
    private Long id;

    @Schema(description = "사용자의 공유 프로필 URL", example = "https://lh3.googleusercontent.com/...")
    private String pictureUrl;

    @Schema(description = "사용자의 닉네임", example = "DongDong")
    private String nickname;

    @Schema(description = "사용자의 이름", example = "홍길동")
    private String name;

    @Builder
    public ProfileResponseDto(Long id, String pictureUrl, String nickname, String name){
        this.id = id;
        this.pictureUrl = pictureUrl;
        this.nickname = nickname;
        this.name = name;
    }
}
