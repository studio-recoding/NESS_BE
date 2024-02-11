package Ness.Backend.oAuth;

import Ness.Backend.auth.AuthDetails;
import Ness.Backend.auth.jwt.JwtTokenProvider;
import Ness.Backend.domain.Member;
import Ness.Backend.member.MemberRepository;
import Ness.Backend.oAuth.dto.GoogleResponseDto;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Service
@RequiredArgsConstructor
@Transactional
public class OAuthService {
    private final Environment env;
    private final RestTemplate restTemplate = new RestTemplate();

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    public GoogleResponseDto socialLogin(String code, String registration) {
        String accessToken = getAccessToken(code, registration);
        JsonNode userResourceNode = getUserResource(accessToken, registration);
        String id = userResourceNode.get("id").asText();
        String email = userResourceNode.get("email").asText();
        String picture = userResourceNode.get("picture").asText();
        String message = checkSignUp(email, id);

        GoogleResponseDto googleResponseDto = GoogleResponseDto.builder()
                .picture(picture)
                .email(email)
                .build();

        return googleResponseDto;
    }

    public String checkSignUp(String email, String password){
        //이런 코드로도 괜찮은 것인가...

        /* 사용자가 제출한 이메일과 비밀번호 확인하기 */
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, password);

        /* 사용자 인증 완료 */
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        /* 인증이 되었을 경우 */
        if(authentication.isAuthenticated()) {
            /* 사용자가 인증되면 AuthDetails 객체가 생성되어 Authentication 객체에 포함되고,
             * 이 AuthDetails 객체를 통해서 인증된 사용자의 정보를 확인 가능 */
            AuthDetails authDetails = (AuthDetails) authentication.getPrincipal();

            Long authenticatedId  = authDetails.getMember().getId();
            String authenticatedEmail = authDetails.getMember().getEmail();

            /* JWT 토큰 반환 */
            return jwtTokenProvider.generateJwtToken(authenticatedId, authenticatedEmail);
        }
        else{ /* 인증이 되지 않았을 경우 */
            try {
                Member member = Member.builder()
                        .email(email)
                        .password(bCryptPasswordEncoder.encode(password)) //비밀번호는 해싱해서 DB에 저장
                        .build();

                memberRepository.save(member);

                return "회원가입이 완료되었습니다.";
            } catch (DataIntegrityViolationException e) {
                /* 중복된 이메일 값이 삽입되려고 할 때 발생하는 예외 처리 */
                return "이미 사용 중인 이메일 주소입니다.";
            }
        }
    }

    private String getAccessToken(String authorizationCode, String registration) {
        //여기 그냥 @Value로 바꿀지 고민 중
        String clientId = env.getProperty("oauth2." + registration + ".client-id");
        String clientSecret = env.getProperty("oauth2." + registration + ".client-secret");
        String redirectUri = env.getProperty("oauth2." + registration + ".redirect-uri");
        String tokenUri = env.getProperty("oauth2." + registration + ".token-uri");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", authorizationCode);
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("redirect_uri", redirectUri);
        params.add("grant_type", "authorization_code");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity entity = new HttpEntity(params, headers);

        ResponseEntity<JsonNode> responseNode = restTemplate.exchange(tokenUri, HttpMethod.POST, entity, JsonNode.class);
        JsonNode accessTokenNode = responseNode.getBody();
        return accessTokenNode.get("access_token").asText();
    }

    private JsonNode getUserResource(String accessToken, String registration) {
        String resourceUri = env.getProperty("oauth2." + registration + ".resource-uri");

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity entity = new HttpEntity(headers);
        return restTemplate.exchange(resourceUri, HttpMethod.GET, entity, JsonNode.class).getBody();
    }
}
