package Ness.Backend.domain.auth.oAuth;

import Ness.Backend.domain.auth.inmemory.RefreshTokenRepository;
import Ness.Backend.domain.auth.oAuth.dto.GoogleResourceDto;
import Ness.Backend.domain.auth.oAuth.dto.GoogleTokenDto;
import Ness.Backend.domain.auth.security.AuthDetails;
import Ness.Backend.domain.auth.jwt.JwtTokenProvider;
import Ness.Backend.domain.member.MemberService;
import Ness.Backend.domain.profile.ProfileRepository;
import Ness.Backend.domain.profile.entity.Profile;
import Ness.Backend.domain.member.entity.Member;
import Ness.Backend.domain.member.MemberRepository;
import Ness.Backend.global.auth.oAuth.dto.GoogleOAuthApi;
import Ness.Backend.global.auth.oAuth.dto.GoogleResourceApi;
import Ness.Backend.global.error.ErrorCode;
import Ness.Backend.global.error.exception.UnauthorizedException;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Service
@RequiredArgsConstructor
@Transactional
public class OAuth2Service {
    private final Environment env;

    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;
    private final GoogleOAuthApi googleOAuthApi;
    private final GoogleResourceApi googleResourceApi;

    public String devSocialLogin(String code, String registration) {
        String accessToken = getAccessToken(code, registration);
        GoogleResourceDto googleResourceDto = getUserResource(accessToken, registration);
        String id = googleResourceDto.getId();
        String email = googleResourceDto.getEmail();
        String picture = googleResourceDto.getPicture();
        String nickname = googleResourceDto.getNickname();

        if (checkSignUp(email)){
            return "로그인에 성공했습니다, "+jwtTokenProvider.generateJwtToken(email).getJwtAccessToken();
        } else {
            socialSignUp(email, id, picture, nickname);
            return "회원가입 및 로그인에 성공했습니다, "+jwtTokenProvider.generateJwtToken(email).getJwtAccessToken();
        }
    }

    public String socialLogin(String code, String registration) {
        /*
        * 로직:
        * 1. client_id, client_secret 등을 사용해 oauth 서버에서 access_token 발급
        * 2. access_token를 통해서 리소스 서버에서 user의 리소스(개인정보) 요청
        * 3. 이미 DB에 존재하는 user인지 확인
        * 3-1. 존재하는 유저가 아니라면 email과 id를 각각 아이디와 패스워드로 DB에 저장한 후 jwt 토큰 반환
        * 3-2. 존재하는 유저가 맞다면
         * */
        String accessToken = getAccessToken(code, registration);
        GoogleResourceDto googleResourceDto = getUserResource(accessToken, registration);
        String id = googleResourceDto.getId();
        String email = googleResourceDto.getEmail();
        String picture = googleResourceDto.getPicture();
        String nickname = googleResourceDto.getNickname();

        if (checkSignUp(email)){
            /* 여기서 response 이루어짐 */
            jwtTokenProvider.generateJwtToken(email);
            return "로그인에 성공했습니다.";
        } else {
            socialSignUp(email, id, picture, nickname);
            jwtTokenProvider.generateJwtToken(email);
            return "회원가입 및 로그인에 성공했습니다.";
        }
    }

    public String socialSignUp(String email, String password, String picture, String nickname) {
        /* 프로필 및 맴버 저장 */
        try {
            memberService.createMember(email, password, picture, nickname);
            return "회원가입이 완료되었습니다.";

        } catch (DataIntegrityViolationException e) {
            /* 중복된 이메일 값이 삽입되려고 할 때 발생하는 예외 처리,unique = true 때문에 발생하는 에러 */
            return "이미 사용 중인 이메일 주소입니다.";
        }
    }

    public Boolean checkSignUp(String email){
        Member member = memberRepository.findMemberByEmail(email);
        return member != null;
    }

    /* oauth 서버에서 access_token 받아옴 */
    private String getAccessToken(String authorizationCode, String registration) {
        String clientId = env.getProperty("oauth2." + registration + ".client-id");
        String clientSecret = env.getProperty("oauth2." + registration + ".client-secret");
        String redirectUri = env.getProperty("oauth2." + registration + ".redirect-uri");
        String tokenUri = env.getProperty("oauth2." + registration + ".token-uri");

        GoogleTokenDto tokenDto = googleOAuthApi.googleGetToken(authorizationCode, clientId, clientSecret, redirectUri, "authorization_code");

        return tokenDto.getAccessToken();
    }

    /* 리다이렉트 URL을 통해서 리소스 가져옴 */
    private GoogleResourceDto getUserResource(String accessToken, String registration) {
        String resourceUri = env.getProperty("oauth2." + registration + ".resource-uri");

        return googleResourceApi.googleGetResource("Bearer " + accessToken);
    }

    public void logout(Member member) {
        /* refreshToken 만료 여부 확인 */
        if (!refreshTokenRepository.existsById(member.getId())) {
            throw new UnauthorizedException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        refreshTokenRepository.deleteById(member.getId());
        SecurityContextHolder.clearContext();
    }

    public void withdrawal(Member member) {
        memberService.deleteMember(member);
    }

    /*
    // 추후 구현 예정
    public void reIssuance(Member member) {
        // refreshToken 유효성 확인
        if (!jwtTokenProvider.validJwtToken(member.getId())) {
            throw new UnauthorizedException(ErrorCode.INVALID_TOKEN);
        }

        // refreshToken 만료 여부 확인
        if (!refreshTokenRepository.existsById(member.getId())) {
            throw new UnauthorizedException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        jwtTokenProvider.generateJwtToken(member.getEmail());
    }
    */
}
