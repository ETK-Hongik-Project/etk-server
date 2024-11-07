package Hongik.EyeTracking.json.controller;

import Hongik.EyeTracking.auth.interfaces.CurrentUserUsername;
import Hongik.EyeTracking.common.response.BaseResponse;
import Hongik.EyeTracking.json.dto.request.JsonUploadRequestDto;
import Hongik.EyeTracking.json.dto.response.JsonResponseDto;
import Hongik.EyeTracking.json.service.JsonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static Hongik.EyeTracking.common.response.HttpResponse.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Slf4j
public class JsonController {
    private final JsonService jsonService;

    @Operation(summary = "로그인 한 유저의 Json 저장")
    @ApiResponses(value = {
            @ApiResponse(responseCode = CREATED, description = "Json 성공적 추가"),
            @ApiResponse(responseCode = NOT_FOUND, description = "해당 username을 가지는 유저가 존재하지 않는 경우")
    })
    @PostMapping("/json")
    public ResponseEntity<JsonResponseDto> uploadJson(@CurrentUserUsername String username, @RequestBody JsonUploadRequestDto requestDto) {
        String json = requestDto.getData();
        JsonResponseDto response = jsonService.createJson(username, json);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "로그인 한 유저의 특정 Json 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = OK, description = "Json 성공적 조회"),
            @ApiResponse(responseCode = NOT_FOUND, description = "해당 username을 가지는 유저가 존재하지 않는 경우"),
            @ApiResponse(responseCode = BAD_REQUEST, description = "잘못된 PK 혹은 유저의 json이 아님")
    })
    @GetMapping("/json/{jsonId}")
    public ResponseEntity<JsonResponseDto> readJson(@CurrentUserUsername String username, @PathVariable Long jsonId) {
        JsonResponseDto responseDto = jsonService.getJson(username, jsonId);

        return ResponseEntity.ok().body(responseDto);
    }

    @Operation(summary = "로그인 한 유저의 모든 Json 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = OK, description = "Json 성공적 조회"),
            @ApiResponse(responseCode = NOT_FOUND, description = "해당 username을 가지는 유저가 존재하지 않는 경우"),
    })
    @GetMapping("/json")
    public ResponseEntity<List<JsonResponseDto>> readJsons(@CurrentUserUsername String username) {
        List<JsonResponseDto> responseDtos = jsonService.getJsons(username);

        return ResponseEntity.ok().body(responseDtos);
    }

    @Operation(summary = "로그인 한 유저의 특정 Json 제거")
    @ApiResponses(value = {
            @ApiResponse(responseCode = OK, description = "Json 성공적 조회"),
            @ApiResponse(responseCode = NOT_FOUND, description = "해당 username을 가지는 유저가 존재하지 않는 경우"),
    })
    @DeleteMapping("/json/{jsonId}")
    public ResponseEntity<BaseResponse> deleteJson(@CurrentUserUsername String username, @PathVariable Long jsonId) {
        jsonService.deleteJson(username, jsonId);

        return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.createSuccessWithNoContent());
    }

    @Operation(summary = "로그인 한 유저의 모든 Json 제거")
    @ApiResponses(value = {
            @ApiResponse(responseCode = OK, description = "Json 성공적 조회"),
            @ApiResponse(responseCode = NOT_FOUND, description = "해당 username을 가지는 유저가 존재하지 않는 경우"),
    })
    @DeleteMapping("/json")
    public ResponseEntity<BaseResponse> deleteJsons(@CurrentUserUsername String username) {
        jsonService.deleteAllJsons(username);

        return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.createSuccessWithNoContent());
    }
}
