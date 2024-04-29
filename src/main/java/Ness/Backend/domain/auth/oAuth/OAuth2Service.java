package Ness.Backend.domain.auth.oAuth;

import Ness.Backend.domain.auth.dto.ResourceDto;
import Ness.Backend.domain.auth.inmemory.RefreshTokenRepository;
import Ness.Backend.domain.auth.jwt.JwtTokenProvider;
import Ness.Backend.domain.auth.jwt.entity.JwtToken;
import Ness.Backend.domain.auth.oAuth.google.dto.GoogleResourceDto;
import Ness.Backend.domain.auth.oAuth.kakao.dto.KakaoResourceDto;
import Ness.Backend.domain.auth.oAuth.naver.dto.NaverResourceDto;
import Ness.Backend.domain.member.MemberRepository;
import Ness.Backend.domain.member.MemberService;
import Ness.Backend.domain.member.entity.Member;
import Ness.Backend.global.auth.oAuth.google.GoogleOAuthApi;
import Ness.Backend.global.auth.oAuth.google.GoogleResourceApi;
import Ness.Backend.global.auth.oAuth.kakao.KakaoOAuthApi;
import Ness.Backend.global.auth.oAuth.kakao.KakaoResourceApi;
import Ness.Backend.global.auth.oAuth.naver.NaverOAuthApi;
import Ness.Backend.global.auth.oAuth.naver.NaverResourceApi;
import Ness.Backend.global.error.ErrorCode;
import Ness.Backend.global.error.exception.UnauthorizedAccessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;


@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class OAuth2Service {
//    private final Environment env;
//    private final MemberRepository memberRepository;
//    private final RefreshTokenRepository refreshTokenRepository;
//    private final MemberService memberService;
//    private final JwtTokenProvider jwtTokenProvider;
//    private final GoogleOAuthApi googleOAuthApi;
//    private final GoogleResourceApi googleResourceApi;
//    private final KakaoOAuthApi kakaoOAuthApi;
//    private final KakaoResourceApi kakaoResourceApi;
//    private final NaverOAuthApi naverOAuthApi;
//    private final NaverResourceApi naverResourceApi;
//    private final AuthenticationManagerBuilder authenticationManagerBuilder;
//
//    public String devSocialLogin(String code, String registration) {
//        String accessToken = getOAuthAccessToken(code, registration);
//        ResourceDto resourceDto = getUserResource(accessToken, registration);
//        String id = resourceDto.getId();
//        String email = resourceDto.getEmail();
//        String picture = resourceDto.getPicture();
//        String nickname = resourceDto.getNickname();
//        String name = resourceDto.getName();
//
//        if (checkSignUp(email)){
//            return registration+" 로그인에 성공했습니다, "+jwtTokenProvider.generateJwtToken(email).getJwtAccessToken();
//        } else {
//            socialSignUp(email, id, picture, nickname, name);
//            return registration+" 회원가입 및 로그인에 성공했습니다, "+jwtTokenProvider.generateJwtToken(email).getJwtAccessToken();
//        }
//    }
//
//    public JwtToken socialLogin(String code, String registration) {
//        /*
//        * 로직:
//        * 1. client_id, client_secret 등을 사용해 oauth 서버에서 access_token 발급
//        * 2. access_token를 통해서 리소스 서버에서 user의 리소스(개인정보) 요청
//        * 3. 이미 DB에 존재하는 user인지 확인
//        * 3-1. 존재하는 유저가 아니라면 email과 id를 각각 아이디와 패스워드로 DB에 저장한 후 jwt 토큰 반환
//        * 3-2. 존재하는 유저가 맞다면
//         * */
//        String accessToken = getOAuthAccessToken(code, registration);
//        ResourceDto resourceDto = getUserResource(accessToken, registration);
//        String id = resourceDto.getId();
//        String email = resourceDto.getEmail();
//        String picture = resourceDto.getPicture();
//        String nickname = resourceDto.getNickname();
//        String name = resourceDto.getName();
//
//        if (checkSignUp(email)){
//            log.info("로그인하는 경우");
//            return getToken(email);
//        } else {
//            socialSignUp(email, id, picture, nickname, name);
//            log.info("회원가입하는 경우");
//            return getToken(email);
//        }
//    }
//
//    public String socialSignUp(String email, String password, String picture, String nickname, String name) {
//        /* 프로필 및 맴버 저장 */
//        try {
//            memberService.createMember(email, password, picture, nickname, name);
//            return "회원가입이 완료되었습니다.";
//
//        } catch (DataIntegrityViolationException e) {
//            /* 중복된 이메일 값이 삽입되려고 할 때 발생하는 예외 처리,unique = true 때문에 발생하는 에러 */
//            return "이미 사용 중인 이메일 주소입니다.";
//        }
//    }
//
//    public JwtToken getToken(String email){
//        return jwtTokenProvider.generateJwtToken(email);
//    }
//
//    public Boolean checkSignUp(String email){
//        Member member = memberRepository.findMemberByEmail(email);
//        return member != null;
//    }
//
//    /* oauth 서버에서 access_token 받아옴 */
//    private String getOAuthAccessToken(String authorizationCode, String registration) {
//        log.info("access token 가져오는 로직 호출");
//        String clientId = env.getProperty("oauth2." + registration + ".client-id");
//        String clientSecret = env.getProperty("oauth2." + registration + ".client-secret");
//        String redirectUri = env.getProperty("oauth2." + registration + ".redirect-uri");
//        String accessToken = null;
//
//        switch (registration) {
//            case "google":
//                accessToken = googleOAuthApi
//                        .googleGetToken(authorizationCode, clientId, clientSecret,
//                                redirectUri,
//                                "authorization_code")
//                        .getAccessToken();
//                log.info("구글 accessToken: " + accessToken);
//                break;
//            case "kakao":
//                accessToken = kakaoOAuthApi
//                        .kakaoGetToken(authorizationCode, clientId, clientSecret,
//                                redirectUri,
//                                "authorization_code")
//                        .getAccessToken();
//                break;
//            case "naver":
//                String state = env.getProperty("oauth2." + registration + ".state");
//                accessToken = naverOAuthApi
//                        .naverGetToken(authorizationCode, clientId, clientSecret,
//                                URLEncoder.encode(redirectUri, StandardCharsets.UTF_8),
//                                "authorization_code",
//                                URLEncoder.encode(state, StandardCharsets.UTF_8))
//                        .getAccessToken();
//                break;
//        }
//        log.info("accessToken: " + accessToken);
//        return accessToken;
//    }
//
//    /* 리다이렉트 URL을 통해서 리소스 가져옴 */
//    private ResourceDto getUserResource(String accessToken, String registration) {
//        log.info("유저 리소스 가져오는 로직 호출");
//        ResourceDto resourceDto = null;
//
//        switch (registration) {
//            case "google":
//                GoogleResourceDto googleResourceDto =
//                        googleResourceApi.googleGetResource("Bearer " + accessToken);
//                resourceDto = ResourceDto.builder()
//                        .id(googleResourceDto.getId())
//                        .email(googleResourceDto.getEmail())
//                        .picture(googleResourceDto.getPicture())
//                        .nickname(googleResourceDto.getName())
//                        .name(googleResourceDto.getName())
//                        .build();
//                break;
//            case "kakao":
//                KakaoResourceDto kakaoResourceDto =
//                        kakaoResourceApi.kakaoGetResource(
//                        "Bearer " + accessToken,
//                        "application/x-www-form-urlencoded;charset=utf-8");
//
//                resourceDto = ResourceDto.builder()
//                        .id(kakaoResourceDto.getId())
//                        .email(kakaoResourceDto.getKakaoAccount().getEmail())
//                        .picture(kakaoResourceDto.getKakaoAccount().getKakaoProfile().getPicture())
//                        .nickname(kakaoResourceDto.getKakaoAccount().getKakaoProfile().getNickname())
//                        .name(kakaoResourceDto.getKakaoAccount().getKakaoProfile().getNickname())
//                        .build();
//                log.info("email: "+ resourceDto.getEmail());
//                break;
//            case "naver":
//                NaverResourceDto naverResourceDto =
//                        naverResourceApi.naverGetResource("Bearer " + accessToken);
//                resourceDto = ResourceDto.builder()
//                        .id(naverResourceDto.getNaverResponse().getId())
//                        .email(naverResourceDto.getNaverResponse().getEmail())
//                        .picture(naverResourceDto.getNaverResponse().getPicture())
//                        .nickname(naverResourceDto.getNaverResponse().getNickname())
//                        .name(naverResourceDto.getNaverResponse().getName())
//                        .build();
//                log.info("email: "+ resourceDto.getEmail());
//                break;
//        }
//        return resourceDto;
//    }
//
//    public void logout(Member member) {
//        /* refreshToken 만료 여부 확인 */
//        if (!refreshTokenRepository.existsById(member.getId())) {
//            throw new UnauthorizedAccessException(ErrorCode.INVALID_REFRESH_TOKEN);
//        }
//
//        refreshTokenRepository.deleteById(member.getId());
//        SecurityContextHolder.clearContext();
//    }
//
//    public void withdrawal(Member member) {
//        memberService.deleteMember(member);
//    }
//
//    /*
//    // 추후 구현 예정
//    public void reIssuance(Member member) {
//        // refreshToken 유효성 확인
//        if (!jwtTokenProvider.validJwtToken(member.getId())) {
//            throw new UnauthorizedException(ErrorCode.INVALID_TOKEN);
//        }
//
//        // refreshToken 만료 여부 확인
//        if (!refreshTokenRepository.existsById(member.getId())) {
//            throw new UnauthorizedException(ErrorCode.INVALID_REFRESH_TOKEN);
//        }
//
//        jwtTokenProvider.generateJwtToken(member.getEmail());
//    }
//    */
}
