package Hongik.EyeTracking.board.controller;

import Hongik.EyeTracking.board.dto.request.CreateBoardRequestDto;
import Hongik.EyeTracking.board.dto.response.CreateBoardResponseDto;
import Hongik.EyeTracking.board.dto.response.ReadBoardResponseDto;
import Hongik.EyeTracking.board.service.BoardService;
import Hongik.EyeTracking.common.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static Hongik.EyeTracking.common.response.HttpResponse.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class BoardController {
    private final BoardService boardService;

    @Operation(summary = "board 생성")
    @ApiResponses(value = {
            @ApiResponse(responseCode = CREATED, description = "post 성공적 추가"),
            @ApiResponse(responseCode = BAD_REQUEST, description = "이미 동명의 board가 존재하는 경우")
    })
    @PostMapping("/boards")
    public ResponseEntity<BaseResponse<CreateBoardResponseDto>> createBoard(@Valid @RequestBody CreateBoardRequestDto requestDto) {
        CreateBoardResponseDto response = boardService.createBoard(requestDto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BaseResponse.createSuccess(response));
    }

    @Operation(summary = "키워드로 board 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = OK, description = "board 성공적 조회"),
    })
    @GetMapping("/boards")
    public ResponseEntity<BaseResponse<List<ReadBoardResponseDto>>> readBoard(@RequestParam("keyword") String keyword) {
        List<ReadBoardResponseDto> responses = boardService.getBoards(keyword);

        return ResponseEntity.status(HttpStatus.OK)
                .body(BaseResponse.createSuccess(responses));
    }

    @Operation(summary = "pk로 board 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = OK, description = "board 성공적 조회"),
            @ApiResponse(responseCode = NOT_FOUND, description = "boardId를 가지는 board가 존재하지 않는 경우"),
    })
    @GetMapping("/boards/{boardId}")
    public ResponseEntity<BaseResponse<ReadBoardResponseDto>> readBoard(@PathVariable("boardId") Long boardId) {
        ReadBoardResponseDto response = boardService.getBoard(boardId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(BaseResponse.createSuccess(response));
    }

    @Operation(summary = "pk로 board 제거")
    @ApiResponses(value = {
            @ApiResponse(responseCode = OK, description = "board 성공적 제거"),
            @ApiResponse(responseCode = NOT_FOUND, description = "boardId를 가지는 board가 존재하지 않는 경우"),
    })
    @DeleteMapping("/boards/{boardId}")
    public void deleteBoard(@PathVariable("boardId") Long boardId) {
        boardService.deleteBoard(boardId);
    }
}
