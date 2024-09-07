package Hongik.EyeTracking.weight.controller;

import Hongik.EyeTracking.auth.interfaces.CurrentUserUsername;
import Hongik.EyeTracking.common.response.BaseResponse;
import Hongik.EyeTracking.weight.dto.WeightResponseDto;
import Hongik.EyeTracking.weight.service.WeightService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static Hongik.EyeTracking.common.response.HttpResponse.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Slf4j
public class WeightController {
    private final WeightService weightService;

    @Operation(summary = "로그인 한 유저의 가중치 저장")
    @ApiResponses(value = {
            @ApiResponse(responseCode = CREATED, description = "가중치 파일 성공적 추가"),
            @ApiResponse(responseCode = NOT_FOUND, description = "해당 username을 가지는 유저가 존재하지 않는 경우")
    })
    @PostMapping(value = "/weight", produces = "application/json", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BaseResponse> uploadWeightFile(@CurrentUserUsername String username, @RequestParam("file") MultipartFile file) throws IOException {
        // 기존 가중치 제거
        weightService.deleteWeightIfExists(username);

        // 새로운 가중치 업로드
        WeightResponseDto responseDto = weightService.createWeight(username, file);

        return ResponseEntity.status(HttpStatus.CREATED).body(BaseResponse.createSuccess(responseDto));
    }

    @Operation(summary = "로그인 한 유저의 가중치 제거")
    @ApiResponses(value = {
            @ApiResponse(responseCode = OK, description = "가중치 파일 성공적 제거"),
            @ApiResponse(responseCode = NOT_FOUND, description = "해당 username을 가지는 유저가 존재하지 않는 경우")
    })
    @DeleteMapping("/weight")
    public ResponseEntity<BaseResponse> deleteWeightFile(@CurrentUserUsername String username) throws IOException {
        weightService.deleteWeight(username);

        return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.createSuccessWithNoContent());
    }

    @Operation(summary = "로그인 한 유저의 가중치 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = OK, description = "가중치 파일 성공적 조회"),
            @ApiResponse(responseCode = NOT_FOUND, description = "해당 username을 가지는 유저가 존재하지 않는 경우, 가중치가 존재하지 않는 경우")
    })
    @GetMapping("/weight")
    public ResponseEntity<byte[]> readWeightFile(@CurrentUserUsername String username) throws IOException{
        byte[] response = weightService.getWeight(username);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
