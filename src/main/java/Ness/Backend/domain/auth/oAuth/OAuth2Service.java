package Ness.Backend.domain.auth.oAuth;

import Ness.Backend.domain.auth.oAuth.dto.GoogleResourceDto;
import Ness.Backend.domain.auth.oAuth.dto.GoogleTokenDto;
import Ness.Backend.domain.auth.security.AuthDetails;
import Ness.Backend.domain.auth.jwt.JwtTokenProvider;
import Ness.Backend.domain.profile.ProfileRepository;
import Ness.Backend.domain.profile.entity.Profile;
import Ness.Backend.domain.member.entity.Member;
import Ness.Backend.domain.member.MemberRepository;
import Ness.Backend.global.auth.oAuth.dto.GoogleOAuthApi;
import Ness.Backend.global.auth.oAuth.dto.GoogleResourceApi;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.util.Pair;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
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
    private final ProfileRepository profileRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final GoogleOAuthApi googleOAuthApi;
    private final GoogleResourceApi googleResourceApi;

    public String socialLogin(String code, String registration) {
        /*
        * 로직:
        * 1. client_id, client_secret 등을 사용해 oauth 서버에서 access_token 발급
        * 2. access_token를 통해서 리소스 서버에서 user의 리소스(개인정보) 요청
        * 3. 이미 DB에 존재하는 user인지 확인
        * 4. 존재하는 유저가 맞을 경우 jwt 토큰 반환, 아니라면 email과 id를 각각 아이디와 패스워드로 DB에 저장
         * */
        String accessToken = getAccessToken(code, registration);
        GoogleResourceDto googleResourceDto = getUserResource(accessToken, registration);
        String id = googleResourceDto.getId();
        String email = googleResourceDto.getEmail();
        String picture = googleResourceDto.getPicture();

        Pair<Boolean, String> result = checkSignUp(email, id);

        if (result.getFirst()){
            return result.getSecond();
        } else {
            return saveMember(email, id, picture);
        }
    }

    public String saveMember(String email, String password, String picture) {
        try {
            Member member = Member.builder()
                    .email(email)
                    .password(bCryptPasswordEncoder.encode(password)) //비밀번호는 해싱해서 DB에 저장
                    .build();

            Profile profile = Profile.builder()
                    .pictureUrl(picture)
                    .member(member)
                    .build();

            memberRepository.save(member);
            profileRepository.save(profile);

            return "회원가입이 완료되었습니다.";

        } catch (DataIntegrityViolationException e) {
            /* 중복된 이메일 값이 삽입되려고 할 때 발생하는 예외 처리,unique = true 때문에 발생하는 에러 */
            return "이미 사용 중인 이메일 주소입니다.";
        }
    }

    public Pair<Boolean, String> checkSignUp(String email, String password){
        /* 사용자가 제출한 이메일과 비밀번호 확인하기 */
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, password);

        /* 사용자 인증 완료 */
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        /* 인증이 되었을 경우 */
        if(authentication.isAuthenticated()) {
            /* 사용자가 인증되면 AuthDetails 객체가 생성되어 Authentication 객체에 포함되고,
             * 이 AuthDetails 객체를 통해서 인증된 사용자의 정보를 확인 가능 */
            AuthDetails authDetails = (AuthDetails) authentication.getPrincipal();

            String authEmail  = authDetails.getMember().getEmail();

            /* 여기서 response 이루어짐 */
            jwtTokenProvider.generateJwtToken(authEmail);

            /* JWT 토큰 반환 */
            return Pair.of(true, "로그인에 성공했습니다.");
        }
        else{ /* 인증이 되지 않았을 경우 */
            return Pair.of(false, "로그인에 실패했습니다. 맴버가 존재하는지 확인해주세요.");
        }
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
}
