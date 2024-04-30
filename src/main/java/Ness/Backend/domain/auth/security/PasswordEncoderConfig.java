package Ness.Backend.domain.auth.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class PasswordEncoderConfig {
    /* 회원가입: 패스워드 암호화를 위해 사용, 의존성 순환 문제 방지를 위해 SecurityConfig에서 분리 */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}