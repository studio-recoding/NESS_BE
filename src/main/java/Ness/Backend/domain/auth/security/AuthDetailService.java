package Ness.Backend.domain.auth.security;

import Ness.Backend.domain.member.MemberRepository;
import Ness.Backend.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class AuthDetailService implements UserDetailsService {
    /* 사용자 정보를 데이터베이스에서 로드 */
    private final MemberRepository memberRepository;

    @Override
    public AuthDetails loadUserByUsername(String email) {
        Member member = memberRepository.findMemberByEmail(email);
        if (member != null){
            return new AuthDetails(member, Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")));
        } else{
            //TODO: null이 아니라 Exception으로 처리 필요
            return null;
        }
    }
}
