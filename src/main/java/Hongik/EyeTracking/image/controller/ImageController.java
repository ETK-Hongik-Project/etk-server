package Hongik.EyeTracking.image.controller;

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

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Slf4j
public class ImageController {
    private final ImageService imageService;
    private static final String OK = "200";
    private static final String CREATED = "201";
    private static final String BAD_REQUEST = "400";
    private static final String NOT_FOUND = "404";

    @Operation(summary = "이미지 저장")
    @ApiResponses(value = {
            @ApiResponse(responseCode = CREATED, description = "즐겨찾기 성공적 추가"),
            @ApiResponse(responseCode = NOT_FOUND, description = "해당 username을 가지는 유저가 존재하지 않는 경우")
    })
    @PostMapping(value = "/users/{username}/images", produces = "application/json", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BaseResponse> uploadImage(@PathVariable("username") String username, @ModelAttribute("file") MultipartFile[] files) {
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

    @Operation(summary = "모든 이미지 제거")
    @ApiResponses(value = {
            @ApiResponse(responseCode = OK, description = "이미지 제거 성공"),
            @ApiResponse(responseCode = NOT_FOUND, description = "해당 username을 가지는 유저가 존재하지 않는 경우")
    })
    @DeleteMapping("/users/{username}/images")
    public ResponseEntity<BaseResponse> deleteImage(@PathVariable("username") String username) {
        imageService.deleteImages(username);

        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @Operation(summary = "모든 이미지 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = OK, description = "이미지 조회 성공"),
            @ApiResponse(responseCode = NOT_FOUND, description = "해당 username을 가지는 유저가 존재하지 않는 경우")
    })
    @GetMapping("/users/{username}/images")
    public ResponseEntity<BaseResponse<List<ImageResponseDto>>> readImages(@PathVariable("username") String username) {
        List<ImageResponseDto> response = imageService.getImages(username);

        return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.createSuccess(response));
    }

    @Operation(summary = "특정 이미지 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = OK, description = "이미지 조회 성공"),
            @ApiResponse(responseCode = NOT_FOUND, description = "해당 username을 가지는 유저가 존재하지 않는 경우")
    })
    @GetMapping(value = "/users/{username}/images/{image_id}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> readImage(@PathVariable("username") String username, @PathVariable("image_id") Long imageId) {
        try {
            byte[] response = imageService.getImage(username, imageId);

            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
