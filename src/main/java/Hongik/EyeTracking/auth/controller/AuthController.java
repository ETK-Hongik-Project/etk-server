package Hongik.EyeTracking.auth.controller;

import Hongik.EyeTracking.auth.dto.LogInRequestDto;
import Hongik.EyeTracking.auth.dto.TokenReIssueResponseDto;
import Hongik.EyeTracking.auth.dto.TokenResponseWithUserUsernameDto;
import Hongik.EyeTracking.auth.service.AuthService;
import Hongik.EyeTracking.common.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@Tag(name = "로그인 관련 컨트롤러")
public class AuthController {
    private final AuthService authService;
    private static final String accessTokenHeader = "Authorization";
    private static final String refreshTokenHeader = "refreshToken";

    @PostMapping("/login")
    @Operation(summary = "username, password 기반 로그인")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "로그인 성공"),
            @ApiResponse(responseCode = "400", description = "아이디 or 비밀번호 틀림"),
    })
    public ResponseEntity<BaseResponse> login(@RequestBody @Valid LogInRequestDto requestDto) {
        TokenResponseWithUserUsernameDto response = authService.login(requestDto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .header(accessTokenHeader, response.getAccessToken())
                .header(refreshTokenHeader, response.getRefreshToken())
                .body(BaseResponse.createSuccess("로그인 성공"));
    }

    @PostMapping("/logout")
    @Operation(summary = "로그아웃",description = "로그아웃을 한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "로그아웃 성공"),
            @ApiResponse(responseCode = "400",description = "accessToken 이 만료되거나 틀렸을 때")
    })
    public ResponseEntity<BaseResponse> logout(@RequestHeader("accessToken") String accessToken) {
        authService.logout(accessToken);
        return ResponseEntity.status(HttpStatus.OK)
                .body(BaseResponse.createSuccess("로그아웃 성공"));
    }

    @PostMapping("/reissue")
    @Operation(summary = "토큰 재발행",description = "accessToken 의 만료로 지정된 자원에 접근할 수 없을 떄 사용, refreshToken 을 기반으로 accessToken 재발행")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "토큰 재발행 성공"),
            @ApiResponse(responseCode = "400",description = "refreshToken 이 만료되거나 틀렸을 때")
    })
    public ResponseEntity<BaseResponse> reIssue(@RequestHeader("accessToken") String accessToken,
                                                @RequestHeader("refreshToken") String refreshToken) {

        TokenReIssueResponseDto response = authService.reIssue(accessToken, refreshToken);

        return ResponseEntity.status(HttpStatus.OK)
                .header(accessTokenHeader, response.getAccessToken())
                .header(refreshTokenHeader, response.getRefreshToken())
                .body(BaseResponse.createSuccess("토큰 재발행"));
    }
}
