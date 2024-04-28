package Ness.Backend.domain.auth.oAuth;


import Ness.Backend.domain.auth.security.AuthDetails;
import Ness.Backend.domain.member.MemberRepository;
import Ness.Backend.domain.member.MemberService;
import Ness.Backend.domain.member.entity.Member;
import Ness.Backend.domain.profile.entity.Profile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class OAuth2CustomUserService extends DefaultOAuth2UserService {
    private final MemberRepository memberRepository;
    private final MemberService memberService;

    /* OAuth2 로그인 요청에 필요한 정보(클라이언트 등록 정보, 사용자의 권한 부여 코드, 액세스 토큰)를 가지고 있는 객체 OAuth2UserRequest*/
    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        /*리소스 서버에 사용자 정보 요청 후 리소스 객체(OAuth2User) 받아오기*/
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);

        Map<String, Object> attributes = oAuth2User.getAttributes();

        /*공급자 정보인 registrationId(구글, 카카오, 네이버)*/
        String registrationId = oAuth2UserRequest.getClientRegistration().getRegistrationId();

        /*사용자 정보 엔드포인트인 userInfoEndpoint*/
        ClientRegistration.ProviderDetails.UserInfoEndpoint userInfoEndpoint = oAuth2UserRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint();

        /*oauth 속 개인정보*/
        Map<String, Object> properties = (Map<String, Object>) attributes.get("properties");
        log.info((String) properties.get("email"));

        Member member;
        /*회원 가입 여부 확인 및 RefreshToken update*/
        if (!memberRepository.existsByEmail(oAuth2User.getName())) {
            memberService.createMember(
                    (String) properties.get("email"),
                    (String) properties.get("id"),
                    (String) properties.get("picture"), //프로필 URL
                    (String) properties.get("name"),    // 닉네임
                    (String) properties.get("name"));   //이름
            member = memberRepository.findMemberByEmail(oAuth2User.getName());
        } else {
            member = memberRepository.findMemberByEmail(oAuth2User.getName());
        }

        log.info(member.getMemberRole().getRole());
        return new AuthDetails(member, attributes, Collections.singleton(new SimpleGrantedAuthority(member.getMemberRole().getRole())));
    }
}
