package Ness.Backend.auth;

import Ness.Backend.auth.dto.LoginRequestDto;
import Ness.Backend.auth.dto.RegisterRequestDto;
import Ness.Backend.auth.jwt.JwtTokenProvider;
import Ness.Backend.domain.Member;
import Ness.Backend.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @Transactional
    public String registerMember(RegisterRequestDto registerRequestDto) {
        try {
            /* 빌더 패턴을 사용해 MemberRole을 넘겨주지 않아도 객체 생성 가능 */
            Member member = Member.builder()
                    .email(registerRequestDto.getEmail())
                    .password(bCryptPasswordEncoder.encode(registerRequestDto.getPassword())) //비밀번호는 해싱해서 DB에 저장
                    .build();

            memberRepository.save(member);

            return "회원가입이 완료되었습니다.";
        } catch (DataIntegrityViolationException e) {
            /* 중복된 이메일 값이 삽입되려고 할 때 발생하는 예외 처리 */
            return "이미 사용 중인 이메일 주소입니다.";
        }
    }

    @Transactional
    public String loginMember(LoginRequestDto loginRequestDto) {
        String email = loginRequestDto.getEmail();
        String password = loginRequestDto.getPassword();

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

        return "로그인에 실패했습니다. 이메일 또는 비밀번호가 일치하는지 확인해주세요.";
    }
}
