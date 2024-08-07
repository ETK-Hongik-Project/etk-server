package Hongik.EyeTracking.image.controller;

import Hongik.EyeTracking.auth.interfaces.CurrentUserUsername;
import Hongik.EyeTracking.common.response.BaseResponse;
import Hongik.EyeTracking.image.dto.ImageResponseDto;
import Hongik.EyeTracking.image.service.ImageService;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static Hongik.EyeTracking.common.response.HttpResponse.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Slf4j
public class ImageController {
    private final ImageService imageService;

    @Operation(summary = "로그인 한 유저의 이미지 저장")
    @ApiResponses(value = {
            @ApiResponse(responseCode = CREATED, description = "이미지 성공적 추가"),
            @ApiResponse(responseCode = NOT_FOUND, description = "해당 username을 가지는 유저가 존재하지 않는 경우")
    })
    @PostMapping(value = "/images", produces = "application/json", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    // swagger 사용하려면 @RequestParam("file")에서 @ModelAttribute("file")로 변경해야함
    public ResponseEntity<BaseResponse> uploadImage(@CurrentUserUsername String username, @RequestParam("file") MultipartFile[] files) {
        List<ImageResponseDto> response = new ArrayList<>();
        Arrays.stream(files).forEach(file -> {
            try {
                response.add(imageService.createImage(username, file));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        return ResponseEntity.status(HttpStatus.CREATED).body(BaseResponse.createSuccess(response));
    }

    @Operation(summary = "로그인 한 유저의 모든 이미지 제거")
    @ApiResponses(value = {
            @ApiResponse(responseCode = OK, description = "이미지 제거 성공"),
            @ApiResponse(responseCode = NOT_FOUND, description = "해당 username을 가지는 유저가 존재하지 않는 경우")
    })
    @DeleteMapping("/images")
    public ResponseEntity<BaseResponse> deleteImage(@CurrentUserUsername String username) {
        imageService.deleteImages(username);

        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @Operation(summary = "로그인 한 유저의 모든 이미지 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = OK, description = "이미지 조회 성공"),
            @ApiResponse(responseCode = NOT_FOUND, description = "해당 username을 가지는 유저가 존재하지 않는 경우")
    })
    @GetMapping("/images")
    public ResponseEntity<BaseResponse<List<ImageResponseDto>>> readImages(@CurrentUserUsername String username) {
        List<ImageResponseDto> response = imageService.getImages(username);

        return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.createSuccess(response));
    }

    @Operation(summary = "로그인 한 유저의 특정 이미지 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = OK, description = "이미지 조회 성공"),
            @ApiResponse(responseCode = NOT_FOUND, description = "해당 username을 가지는 유저가 존재하지 않는 경우")
    })
    @GetMapping(value = "/images/{image_id}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> readImage(@CurrentUserUsername String username, @PathVariable("image_id") Long imageId) {
        try {
            byte[] response = imageService.getImage(username, imageId);

            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
