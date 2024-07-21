package Hongik.EyeTracking.post.controller;

import Hongik.EyeTracking.board.dto.response.ReadBoardResponseDto;
import Hongik.EyeTracking.board.service.BoardService;
import Hongik.EyeTracking.common.response.BaseResponse;
import Hongik.EyeTracking.post.dto.request.CreatePostRequestDto;
import Hongik.EyeTracking.post.dto.request.ModifyPostRequestDto;
import Hongik.EyeTracking.post.dto.response.CreatePostResponseDto;
import Hongik.EyeTracking.post.dto.response.ModifyPostResponseDto;
import Hongik.EyeTracking.post.dto.response.ReadPostResponseDto;
import Hongik.EyeTracking.post.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static Hongik.EyeTracking.common.response.HttpResponse.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @Operation(summary = "post 생성")
    @ApiResponses(value = {
            @ApiResponse(responseCode = CREATED, description = "post 성공적 추가"),
            @ApiResponse(responseCode = NOT_FOUND, description = "해당 username을 가지는 유저가 존재하지 않는 경우, boardId를 가지는 board가 존재하지 않는 경우")
    })
    @PostMapping("/users/{username}/boards/{boardId}/posts")
    public ResponseEntity<BaseResponse<CreatePostResponseDto>> createPost(@PathVariable("username") String username, @PathVariable("boardId") Long boardId,
                                                                          @Valid @RequestBody CreatePostRequestDto requestDto) {
        CreatePostResponseDto response = postService.createPost(username, boardId, requestDto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BaseResponse.createSuccess(response));
    }

    @Operation(summary = "PK로 post 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = OK, description = "post 성공적 조회"),
            @ApiResponse(responseCode = NOT_FOUND, description = "postId를 가지는 post가 존재하지 않는 경우")
    })
    @GetMapping("/posts/{postId}")
    public ResponseEntity<BaseResponse<ReadPostResponseDto>> readPost(@PathVariable("postId") Long postId) {
        ReadPostResponseDto response = postService.getPost(postId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(BaseResponse.createSuccess(response));
    }

    @Operation(summary = "board의 post 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = OK, description = "post 성공적 조회"),
            @ApiResponse(responseCode = NOT_FOUND, description = "boardId를 가지는 board가 존재하지 않는 경우, postId를 가지는 post가 존재하지 않는 경우")
    })
    @GetMapping("/boards/{boardId}/posts")
    public ResponseEntity<BaseResponse<List<ReadPostResponseDto>>> readPostsOfBoard(@PathVariable("boardId") Long boardId, @RequestParam(value = "keyword", required = false) String keyword,
                                                                                    @RequestParam("pageNo") int pageNo) {
        if (keyword == null) {
            List<ReadPostResponseDto> responses = postService.getPostsOfBoard(boardId, pageNo);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(BaseResponse.createSuccess(responses));
        } else {
            List<ReadPostResponseDto> responses = postService.getPostsOfBoard(boardId, pageNo, keyword);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(BaseResponse.createSuccess(responses));
        }
    }

    @Operation(summary = "user의 post 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = OK, description = "post 성공적 조회"),
            @ApiResponse(responseCode = NOT_FOUND, description = "username을 가지는 user가 존재하지 않는 경우, postId를 가지는 post가 존재하지 않는 경우")
    })
    @GetMapping("/users/{username}/posts")
    public ResponseEntity<BaseResponse<List<ReadPostResponseDto>>> readPostsOfUser(@PathVariable("username") String username, @RequestParam("pageNo") int pageNo) {
        List<ReadPostResponseDto> responses = postService.getPostsOfUser(username, pageNo);

        return ResponseEntity.status(HttpStatus.OK)
                    .body(BaseResponse.createSuccess(responses));
    }

    @Operation(summary = "post 수정")
    @ApiResponses(value = {
            @ApiResponse(responseCode = OK, description = "post 성공적 수정"),
            @ApiResponse(responseCode = NOT_FOUND, description = "username을 가지는 user가 존재하지 않는 경우, postId를 가지는 post가 존재하지 않는 경우"),
            @ApiResponse(responseCode = BAD_REQUEST, description = "user의 post가 아닌 경우")
    })
    @PostMapping("/users/{username}/posts/{postId}")
    public ResponseEntity<BaseResponse<ModifyPostResponseDto>> modifyPost(@PathVariable("username") String username, @PathVariable("postId") Long postId,
                                                                          @Valid @RequestBody ModifyPostRequestDto requestDto) {
        ModifyPostResponseDto response = postService.updatePost(username, postId, requestDto);

        return ResponseEntity.status(HttpStatus.OK)
                .body(BaseResponse.createSuccess(response));
    }

    @Operation(summary = "post 제거")
    @ApiResponses(value = {
            @ApiResponse(responseCode = OK, description = "post 성공적 제거"),
            @ApiResponse(responseCode = NOT_FOUND, description = "username을 가지는 user가 존재하지 않는 경우, postId를 가지는 post가 존재하지 않는 경우"),
            @ApiResponse(responseCode = BAD_REQUEST, description = "user의 post가 아닌 경우")
    })
    @DeleteMapping("/users/{username}/posts/{postId}")
    public ResponseEntity<BaseResponse> deletePost(@PathVariable("username") String username, @PathVariable("postId") Long postId) {
        postService.deletePost(username, postId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(BaseResponse.createSuccessWithNoContent());
    }
}
