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
import java.util.Objects;

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
        for (Map.Entry<String, Object> entry : attributes.entrySet()) {
            System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue());
        }

        /*공급자 정보인 registrationId(구글, 카카오, 네이버)*/
        String registrationId = oAuth2UserRequest.getClientRegistration().getRegistrationId();
        log.info(registrationId);

        /*사용자 정보 엔드포인트인 userInfoEndpoint*/
        ClientRegistration.ProviderDetails.UserInfoEndpoint userInfoEndpoint = oAuth2UserRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint();

        String id = null;
        String email = null;
        String picture = null;
        String nickname = null;
        String name = null;

        if (Objects.equals(registrationId, "google")){
            id = (String) attributes.get("id");
            email = (String) attributes.get("email");
            picture = (String) attributes.get("picture");
            nickname = (String) attributes.get("name");
            name = (String) attributes.get("name");
        }

        Member member;
        /*이메일로 회원 가입 여부 확인*/
        if (!memberRepository.existsByEmail(email)) {
            memberService.createMember(email, id, picture, nickname, name);
        }
        member = memberRepository.findMemberByEmail(email);

        log.info(member.getMemberRole().getRole());
        return new AuthDetails(member, attributes, Collections.singleton(new SimpleGrantedAuthority(member.getMemberRole().getRole())));
    }

//    -------------google response------------
//    {
//        "id" : "00000000000000000000",
//        "email" : "sample@gmail.com",
//        "verified_email" : true,
//        "name" : "홍길동",
//        "given_name" : "길동",
//        "family_name" : "홍",
//        "picture" : "https://url 경로",
//        "locale" : "ko"
//    }
}
