package Ness.Backend.domain.auth.security;

import Ness.Backend.domain.auth.inmemory.RefreshTokenService;
import Ness.Backend.domain.auth.jwt.JwtAuthenticationFilter;
import Ness.Backend.domain.auth.jwt.JwtAuthorizationFilter;
import Ness.Backend.domain.auth.jwt.JwtTokenProvider;
import Ness.Backend.domain.auth.oAuth.OAuth2CustomUserService;
import Ness.Backend.domain.auth.oAuth.OAuthSuccessHandler;
import Ness.Backend.domain.member.MemberRepository;
import jakarta.servlet.DispatcherType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.time.Duration;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final MemberRepository memberRepository;
    private final AuthDetailService authDetailService;
    private final OAuth2CustomUserService oAuth2CustomUserService;
    private final AuthenticationConfiguration authenticationConfiguration;
    private final RefreshTokenService refreshTokenService;

    /* 로그인: 사용자의 자격 증명을 검증 및 권한 부여 */
    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public OAuthSuccessHandler oAuthSuccessHandler(){
        return new OAuthSuccessHandler(jwtTokenProvider());
    }

    /* 로그인: 사용자 정보(memberRepository 내용)를 토대로 토큰을 생성하거나 검증 */
    @Bean
    public JwtTokenProvider jwtTokenProvider() {
        return new JwtTokenProvider(memberRepository);
    }

    /* CORS 구성을 URL 패턴에 따라 적용
     * CORS란? 교차 출처 자원 공유(Cross-Origin Resource Sharing)의 약어로, 기본적으로 하나의 도메인 리소스만 접근 가능하다.
     * 그러나 FE가 여러 도메인의 리소스에 동시에 접근해야 하는 경우, BE에서 CORS를 허용해 주어야 한다.*/
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("http://localhost:3000");
        configuration.addAllowedOrigin("http://localhost:8080");
        configuration.addAllowedOrigin("https://nessplanning.com");
        configuration.addAllowedOrigin("https://nessplanning.com:3000");
        configuration.addAllowedOriginPattern("https://*.nessplanning.com");
        configuration.addAllowedOriginPattern("https://*.nessplanning.com:8080");
        configuration.addAllowedOriginPattern("https://*.nessplanning.com:3000");
        configuration.addAllowedOriginPattern("https://www.nessplanning.com");
        configuration.addAllowedOriginPattern("https://api.nessplanning.com:8080");
        configuration.addAllowedMethod("*"); //모든 Method 허용(POST, GET, ...)
        configuration.addAllowedHeader("*"); //모든 Header 허용
        configuration.setMaxAge(Duration.ofSeconds(3600)); //브라우저가 응답을 캐싱해도 되는 시간(1시간)
        configuration.setAllowCredentials(true); //CORS 요청에서 자격 증명(쿠키, HTTP 헤더) 허용
        configuration.addExposedHeader("Authorization"); // 클라이언트가 특정 헤더값에 접근 가능하도록 하기
        configuration.addExposedHeader("Authorization-Refresh");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", configuration); //위에서 설정한 Configuration 적용
        return source;
    }

    /* 여러 개의 보안 필터를 조합하여 하나의 보안 체인을 생성 */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(csrf -> csrf.disable()) //악의적인 공격 방어를 위해서 CSRF 토큰 사용 비활성화
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) //CORS 설정
                .formLogin(formLogin -> formLogin.disable()) //폼 기반 로그인을 비활성화->토큰 기반 인증 필요
                .httpBasic(httpBasic -> httpBasic.disable()) //HTTP 기본 인증을 비활성화->비밀번호를 평문으로 보내지 않음
                .sessionManagement(c -> c.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) //세션을 생성하지 않음->토큰 기반 인증 필요
                .addFilter(new JwtAuthenticationFilter(authenticationManager(), jwtTokenProvider(), refreshTokenService))  //사용자 인증
                .addFilter(new JwtAuthorizationFilter(authenticationManager(),  jwtTokenProvider(), authDetailService)) //사용자 권한 부여
                .oauth2Login((oauth2) -> oauth2 //oauth가 성공하면 보내줄 포인트
                        .userInfoEndpoint(userInfoEndpoint -> userInfoEndpoint
                                .userService(oAuth2CustomUserService))
                        .successHandler(oAuthSuccessHandler()))
                .authorizeHttpRequests(requests -> requests
                        .dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll()
                        //.requestMatchers("/signup/**", "/login/**").permitAll() // 회원가입 및 로그인 경로는 인증 생략
                        .anyRequest().permitAll()	//개발 환경: 모든 종류의 요청에 인증 불필요
                        //.anyRequest().authenticated() // 그 외 모든 요청에 대해 인증 필요
                );
        return httpSecurity.build();
    }
}
