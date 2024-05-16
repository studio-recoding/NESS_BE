package Ness.Backend.domain.auth.oAuth;
import Ness.Backend.domain.auth.inmemory.RefreshTokenRepository;
import Ness.Backend.domain.auth.jwt.JwtTokenProvider;
import Ness.Backend.domain.member.MemberService;
import Ness.Backend.domain.member.entity.Member;
import Ness.Backend.global.error.ErrorCode;
import Ness.Backend.global.error.exception.UnauthorizedAccessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class OAuth2Service {
    private final RefreshTokenRepository refreshTokenRepository;
    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    public void logout(Member member) {
        /* refreshToken 만료 여부 확인 */
        if (!refreshTokenRepository.existsById(member.getId())) {
            throw new UnauthorizedAccessException(ErrorCode.INVALID_REFRESH_TOKEN);
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
