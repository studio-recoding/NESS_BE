package Ness.Backend.infra.s3;

import Ness.Backend.domain.member.entity.Member;
import Ness.Backend.global.auth.AuthUser;
import Ness.Backend.infra.s3.dto.GetS3UrlDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "S3 API", description = "S3 사진 업로드 관련 API")
@RequestMapping("/s3")
public class S3Controller {
    private final S3Service s3Service;

    @GetMapping(value = "/posturl")
    @Operation(summary = "post용 presigned url 생성", description = "post용 presigned url을 만드는 API 입니다.")
    public ResponseEntity<GetS3UrlDto> getPostS3Url(@AuthUser Member member) {
        GetS3UrlDto getS3UrlDto = s3Service.getPostS3Url(member.getId());
        return new ResponseEntity<>(getS3UrlDto, HttpStatusCode.valueOf(200));
    }

    @GetMapping(value = "/geturl")
    @Operation(summary = "get용 presigned url 생성", description = "post용 presigned url을 만드는 API 입니다.")
    public ResponseEntity<GetS3UrlDto> getGetS3Url(@AuthUser Member member, @RequestParam String key) {
        GetS3UrlDto getS3UrlDto = s3Service.getGetS3Url(member.getId(), key);
        return new ResponseEntity<>(getS3UrlDto, HttpStatusCode.valueOf(200));
    }
}
