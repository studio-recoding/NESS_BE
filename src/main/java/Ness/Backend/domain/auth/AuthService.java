package Ness.Backend.domain.auth;

import Ness.Backend.domain.auth.dto.request.PostRefreshTokenDto;
import Ness.Backend.domain.auth.dto.response.GetJwtTokenDto;
import Ness.Backend.domain.auth.inmemory.RefreshTokenRepository;
import Ness.Backend.domain.auth.inmemory.RefreshTokenService;
import Ness.Backend.domain.auth.jwt.JwtTokenProvider;
import Ness.Backend.domain.member.entity.Member;
import Ness.Backend.global.error.ErrorCode;
import Ness.Backend.global.error.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final RefreshTokenService refreshTokenService;

    @Transactional
    public void logout(Member member, PostRefreshTokenDto postRefreshTokenDto) {
        /* refreshToken 만료 여부 확인 */
        if(refreshTokenRepository.findRefreshTokenByJwtRefreshToken(postRefreshTokenDto.getJwtRefreshToken()).isEmpty()){
            throw new UnauthorizedException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        refreshTokenService.removeRefreshToken(postRefreshTokenDto.getJwtRefreshToken());
        SecurityContextHolder.clearContext();
    }

    @Transactional
    public GetJwtTokenDto reIssuance(Member member, PostRefreshTokenDto postRefreshTokenDto) {
        /* refreshToken 유효성 확인 */
        String refreshToken = postRefreshTokenDto.getJwtRefreshToken().substring(7);

        if (!jwtTokenProvider.validRefreshToken(refreshToken)) {
            throw new UnauthorizedException(ErrorCode.INVALID_TOKEN);
        }

        /* refreshToken 만료 여부 확인 */
        if(refreshTokenRepository.findRefreshTokenByJwtRefreshToken(postRefreshTokenDto.getJwtRefreshToken()).isEmpty()){
            throw new UnauthorizedException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        final GetJwtTokenDto generateToken = GetJwtTokenDto.builder()
                .jwtAccessToken("Bearer " + jwtTokenProvider.generateAccessToken(member.getEmail(), new Date()))
                .jwtRefreshToken(postRefreshTokenDto.getJwtRefreshToken())
                .build();

        return generateToken;
    }


}
