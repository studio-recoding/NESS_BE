package Ness.Backend.domain.auth.security;

import Ness.Backend.domain.member.MemberRepository;
import Ness.Backend.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthDetailService implements UserDetailsService {
    /* 사용자 정보를 데이터베이스에서 로드 */
    private final MemberRepository memberRepository;

    @Override
    public AuthDetails loadUserByUsername(String email) {
        Member member = memberRepository.findMemberByEmail(email);
        if (member != null){
            return new AuthDetails(member);
        } else{
            return null;
        }
    }
}
