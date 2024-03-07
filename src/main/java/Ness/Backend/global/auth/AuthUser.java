package Ness.Backend.auth.security;

import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)     //런타임에도 유효(해당 어노테이션 없을 시 컴파일에만 유효)
@Parameter(hidden = true)               //API 문서에 적용하지 않음
/* 인증 객체 정보를 가지고 다니는 커스텀 어노테이션, AuthDetail의 필드인 member를 가져온다. */
@AuthenticationPrincipal(expression = "#this == 'anonymousUser' ? null : member")
public @interface AuthUser {
    /* 사용자 객체의 유형이 잘못 된 경우 오류 발생하지 않음
    boolean errorOnInvalidType() default false;

    어노테이션의 표현식(속성 값 결정하게 만듬), 예시: @MyAnnotation(expression = "#{1 + 2}")
    String expression() default "";
    */
}
