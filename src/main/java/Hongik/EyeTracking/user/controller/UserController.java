package Hongik.EyeTracking.user.controller;

import Hongik.EyeTracking.auth.interfaces.CurrentUserUsername;
import Hongik.EyeTracking.common.response.BaseResponse;
import Hongik.EyeTracking.user.domain.User;
import Hongik.EyeTracking.user.dto.request.CreateUserRequestDto;
import Hongik.EyeTracking.user.dto.response.CreateUserResponseDto;
import Hongik.EyeTracking.user.dto.response.ReadUserResponseDto;
import Hongik.EyeTracking.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static Hongik.EyeTracking.common.response.HttpResponse.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @Operation(summary = "회원가입")
    @ApiResponses(value = {
            @ApiResponse(responseCode = CREATED, description = "회원가입 성공"),
            @ApiResponse(responseCode = BAD_REQUEST, description = "이미 사용중인 username, 이미 사용중인 email")
    })
    @PostMapping("/join")
    public ResponseEntity<BaseResponse<CreateUserResponseDto>> join(@Valid @RequestBody CreateUserRequestDto requestDto) {
        CreateUserResponseDto response = userService.createUser(requestDto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BaseResponse.createSuccess(response));
    }

    @Operation(summary = "user 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = OK, description = "user 성공적 조회"),
            @ApiResponse(responseCode = NOT_FOUND, description = "해당 username을 가지는 유저가 존재하지 않는 경우")
    })
    @GetMapping("/user")
    public ResponseEntity<BaseResponse<ReadUserResponseDto>> readUser(@CurrentUserUsername String username) {
        ReadUserResponseDto response = userService.getUser(username);

        return ResponseEntity.status(HttpStatus.OK)
                .body(BaseResponse.createSuccess(response));
    }

    @Operation(summary = "회원 탈퇴")
    @ApiResponses(value = {
            @ApiResponse(responseCode = OK, description = "user 성공적 제거"),
            @ApiResponse(responseCode = NOT_FOUND, description = "해당 username을 가지는 유저가 존재하지 않는 경우")
    })
    @DeleteMapping("/user")
    public ResponseEntity<BaseResponse> deleteUser(@CurrentUserUsername String username) {
        userService.deleteUser(username);

        return ResponseEntity.status(HttpStatus.OK)
                .body(BaseResponse.createSuccessWithNoContent());
    }
}
