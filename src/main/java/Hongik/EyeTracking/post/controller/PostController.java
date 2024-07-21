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
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/users/{username}/boards/{boardId}/posts")
    public ResponseEntity<BaseResponse<CreatePostResponseDto>> createPost(@PathVariable("username") String username, @PathVariable("boardId") Long boardId,
                                                                          @Valid CreatePostRequestDto requestDto) {
        CreatePostResponseDto response = postService.createPost(username, boardId, requestDto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BaseResponse.createSuccess(response));
    }

    @GetMapping("/posts/{postId}")
    public ResponseEntity<BaseResponse<ReadPostResponseDto>> readPost(@PathVariable("postId") Long postId) {
        ReadPostResponseDto response = postService.getPost(postId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(BaseResponse.createSuccess(response));
    }

    @GetMapping("/boards/{boardId}/posts")
    public ResponseEntity<BaseResponse<List<ReadPostResponseDto>>> readPosts(@PathVariable("boardId") Long boardId, @RequestParam(value = "keyword", required = false) String keyword,
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

    @PostMapping("/users/{username}/posts/{postId}")
    public ResponseEntity<BaseResponse<ModifyPostResponseDto>> modifyPost(@PathVariable("username") String username, @PathVariable("postId") Long postId,
                                                                          @Valid ModifyPostRequestDto requestDto) {
        ModifyPostResponseDto response = postService.updatePost(username, postId, requestDto);

        return ResponseEntity.status(HttpStatus.OK)
                .body(BaseResponse.createSuccess(response));
    }

    @DeleteMapping("/users/{username}/posts/{postId}")
    public ResponseEntity<BaseResponse> deletePost(@PathVariable("username") String username, @PathVariable("postId") Long postId) {
        postService.deletePost(username, postId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(BaseResponse.createSuccessWithNoContent());
    }
}
