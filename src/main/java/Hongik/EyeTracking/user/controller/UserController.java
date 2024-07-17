package Hongik.EyeTracking.user.controller;

import Hongik.EyeTracking.common.response.BaseResponse;
import Hongik.EyeTracking.user.domain.User;
import Hongik.EyeTracking.user.dto.request.CreateUserRequestDto;
import Hongik.EyeTracking.user.dto.response.CreateUserResponseDto;
import Hongik.EyeTracking.user.dto.response.ReadUserResponseDto;
import Hongik.EyeTracking.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/join")
    public ResponseEntity<BaseResponse<CreateUserResponseDto>> join(CreateUserRequestDto requestDto) {
        CreateUserResponseDto response = userService.createUser(requestDto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BaseResponse.createSuccess(response));
    }

    @GetMapping("/users/{username}")
    public ResponseEntity<BaseResponse<ReadUserResponseDto>> readUser(@PathVariable("username") String username) {
        ReadUserResponseDto response = userService.getUser(username);

        return ResponseEntity.status(HttpStatus.OK)
                .body(BaseResponse.createSuccess(response));
    }

    @DeleteMapping("/users/{username}")
    public ResponseEntity<BaseResponse> deleteUser(@PathVariable("username") String username) {
        userService.deleteUser(username);

        return ResponseEntity.status(HttpStatus.OK)
                .body(BaseResponse.createSuccessWithNoContent());
    }
}
