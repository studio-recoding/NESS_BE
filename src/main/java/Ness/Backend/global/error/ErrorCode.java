package Ness.Backend.global.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    /* Common */
    INVALID_INPUT_VALUE(BAD_REQUEST, "C001", "Invalid Input Value"),
    _INTERNAL_SERVER_ERROR(INTERNAL_SERVER_ERROR, "C000", "서버 에러, 관리자에게 문의 바랍니다."),
    _BAD_REQUEST(BAD_REQUEST, "C001", "잘못된 요청입니다."),
    _METHOD_NOT_ALLOWED(METHOD_NOT_ALLOWED, "C003", "지원하지 않는 Http Method 입니다."),

    /* Auth */
    UNAUTHORIZED_ACCESS(UNAUTHORIZED, "AUTH000", "권한이 없습니다."),
    EXPIRED_TOKEN(UNAUTHORIZED, "AUTH001", "만료된 엑세스 토큰입니다."),
    INVALID_REFRESH_TOKEN(UNAUTHORIZED, "AUTH002", "리프레시 토큰이 유효하지 않습니다."),
    MISMATCH_REFRESH_TOKEN(UNAUTHORIZED, "AUTH003", "리프레시 토큰의 유저 정보가 일치하지 않습니다."),
    INVALID_AUTH_TOKEN(UNAUTHORIZED, "AUTH004", "권한 정보가 없는 토큰입니다."),
    UNAUTHORIZED_USER(UNAUTHORIZED, "AUTH005", "현재 내 계정 정보가 존재하지 않습니다."),
    REFRESH_TOKEN_NOT_FOUND(NOT_FOUND, "AUTH006", "로그아웃 된 사용자입니다."),
    FORBIDDEN_USER(FORBIDDEN, "AUTH007", "권한이 없는 유저입니다."),
    LOGIN_FAILED(UNAUTHORIZED, "AUTH008", "로그인에 실패했습니다."),
    INVALID_TOKEN(BAD_REQUEST, "AUTH009", "유효하지 않은 토큰입니다."),
    INVALID_TOKEN_SIGNATURE(BAD_REQUEST, "AUTH010", "유효하지 않은 시그니처를 가진 토큰입니다. 온전한 토큰이 맞는지 확인해주세요."),
    TOKEN_ERROR(BAD_REQUEST, "AUTH011", "기타 토큰 에러입니다."),
    INVALID_PRINCIPAL(BAD_REQUEST, "AUTH012", "인증정보가 존재하지 않습니다."),
    OAUTH_ERROR(BAD_REQUEST, "AUTH012", "소셜 로그인 에러입니다."),

    /* 카테고리 관련 */
    INVALID_CATEGORY_NAME(CONFLICT, "CATE001", "해당 카테고리명이 이미 존재합니다. 카테고리명은 중복될 수 없습니다."),
    INVALID_CATEGORY_DELETE(BAD_REQUEST, "CATE002", "미분류 카테고리는 삭제 불가능합니다."),
    NOTFOUND_CATEGORY(BAD_REQUEST, "CATE003", "해당 카테고리가 해당 맴버에게 존재하지 않습니다."),

    /* 리포트 관련 */
    MISMATCH_REPORT_RECOMMEND(BAD_REQUEST, "RPT001", "한 줄 추천이 존재하지 않습니다."),

    /* 스케쥴 관련 */
    NOTFOUND_SCHEDULE(BAD_REQUEST, "SCHE001", "해당 스케쥴이 존재하지 않습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}