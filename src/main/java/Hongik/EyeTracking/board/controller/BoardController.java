package Hongik.EyeTracking.board.controller;

import Hongik.EyeTracking.board.dto.request.CreateBoardRequestDto;
import Hongik.EyeTracking.board.dto.response.CreateBoardResponseDto;
import Hongik.EyeTracking.board.dto.response.ReadBoardResponseDto;
import Hongik.EyeTracking.board.service.BoardService;
import Hongik.EyeTracking.common.response.BaseResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class BoardController {
    private final BoardService boardService;

    @PostMapping("/boards")
    public ResponseEntity<BaseResponse<CreateBoardResponseDto>> createBoard(@Valid CreateBoardRequestDto requestDto) {
        CreateBoardResponseDto response = boardService.createBoard(requestDto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BaseResponse.createSuccess(response));
    }

    @GetMapping("/boards")
    public ResponseEntity<BaseResponse<List<ReadBoardResponseDto>>> readBoard(@RequestParam("keyword") String keyword) {
        List<ReadBoardResponseDto> responses = boardService.getBoards(keyword);

        return ResponseEntity.status(HttpStatus.OK)
                .body(BaseResponse.createSuccess(responses));
    }

    @GetMapping("/boards/{boardId}")
    public ResponseEntity<BaseResponse<ReadBoardResponseDto>> readBoard(@PathVariable("boardId") Long boardId) {
        ReadBoardResponseDto response = boardService.getBoard(boardId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(BaseResponse.createSuccess(response));
    }

    @DeleteMapping("/boards/{boardId}")
    public void deleteBoard(@PathVariable("boardId") Long boardId) {
        boardService.deleteBoard(boardId);
    }
}
