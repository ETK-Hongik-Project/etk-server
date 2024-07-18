package Hongik.EyeTracking.common.response.error;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum ErrorCode {

    /**
     * 400 Bad Request
     */
    BAD_REQUEST("잘못된 요청입니다."),
    ACCESS_TOKEN_NOT_MATCH("엑세스 토큰을 확인해주세요"),
    REFRESH_TOKEN_NOT_MATCH("리프레시 토큰을 확인해주세요"),



    /**
     * 401 Unauthorized
     */
    UNAUTHORIZE("인증에 실패하였습니다."),

    /**
     * 403 Forbidden
     */


    /**
     * 404 Not Found
     */
    NOT_FOUND("존재하지 않는 값입니다."),
    USER_NOT_FOUND("존재하지 않는 유저입니다"),
    BOARD_NOT_FOUND("존재하지 않는 게시판입니다"),
    POST_NOT_FOUND("존재하지 않는 게시물입니다"),


    /**
     * 405 Method Not Allowed


     /**
     * 409 Conflict
     */
    USERNAME_ALREADY_EXISTS("이미 사용중인 아이디입니다"),
    EMAIL_ALREADY_EXISTS("이미 사용중인 이메일입니다"),
    BOARD_ALREADY_EXISTS("이미 존재하는 게시판입니다"),

    /**
     * 500 Internal Server Error
     */
    DATA_NOT_READY("데이터가 준비되지 않았습니다");





    private final String message;
}