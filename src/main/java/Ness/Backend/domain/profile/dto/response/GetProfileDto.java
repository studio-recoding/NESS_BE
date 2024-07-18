package Ness.Backend.domain.profile.dto.response;


import Ness.Backend.domain.profile.entity.PersonaType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GetProfileDto {

    @Schema(description = "프로필 고유 아이디", example = "0")
    private Long id;

    @Schema(description = "사용자의 공유 프로필 URL", example = "https://lh3.googleusercontent.com/...")
    private String pictureUrl;

    @Schema(description = "사용자의 S3 프로필 사진 경로", example = "/profile/1/...")
    private String pictureKey;

    @Schema(description = "사용자의 닉네임", example = "DongDong")
    private String nickname;

    @Schema(description = "사용자의 이름", example = "홍길동")
    private String name;

    @Schema(description = "사용자가 설정한 페르소나", example = "NESS")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private PersonaType persona;

    @Schema(description = "사용자가 설정한 이메일 알림 여부", example = "NESS")
    @JsonProperty("isEmailActive")
    private boolean isEmailActive;

    @Schema(description = "사용자의 이메일", example = "example@gmail.com")
    @JsonProperty("email")
    private String email;

    @Schema(description = "사용자의 온보딩 여부", example = "false")
    @JsonProperty("onBoarding")
    private boolean onBoarding;

    @Builder
    public GetProfileDto(Long id, String pictureUrl, String pictureKey, String nickname, String name,
                         PersonaType persona, boolean isEmailActive, String email, boolean onBoarding){
        this.id = id;
        this.pictureUrl = pictureUrl;
        this.pictureKey = pictureKey;
        this.nickname = nickname;
        this.name = name;
        this.persona = persona;
        this.isEmailActive = isEmailActive;
        this.email = email;
        this.onBoarding = onBoarding;
    }
}
