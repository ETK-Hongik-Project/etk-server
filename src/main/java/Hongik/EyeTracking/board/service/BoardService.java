package Hongik.EyeTracking.board.service;

import Hongik.EyeTracking.board.domain.Board;
import Hongik.EyeTracking.board.dto.request.CreateBoardRequestDto;
import Hongik.EyeTracking.board.dto.response.CreateBoardResponseDto;
import Hongik.EyeTracking.board.dto.response.ReadBoardResponseDto;
import Hongik.EyeTracking.board.repository.BoardRepository;
import Hongik.EyeTracking.comment.domain.Comment;
import Hongik.EyeTracking.comment.repository.CommentRepository;
import Hongik.EyeTracking.common.response.error.ErrorCode;
import Hongik.EyeTracking.common.response.error.exception.DuplicateException;
import Hongik.EyeTracking.common.response.error.exception.NotFoundException;
import Hongik.EyeTracking.post.domain.Post;
import Hongik.EyeTracking.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public CreateBoardResponseDto createBoard(CreateBoardRequestDto requestDto) {
        if (boardRepository.existsByName(requestDto.getName())) {
            throw new DuplicateException(ErrorCode.BOARD_ALREADY_EXISTS);
        }

        Board board = Board.builder()
                .name(requestDto.getName())
                .build();

        boardRepository.save(board);

        return CreateBoardResponseDto.from(board);
    }

    public ReadBoardResponseDto getBoard(Long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(() ->
                new NotFoundException(ErrorCode.BOARD_NOT_FOUND)
        );

        return ReadBoardResponseDto.from(board);
    }

    public List<ReadBoardResponseDto> getBoards(String keyword) {
        List<ReadBoardResponseDto> responses = new ArrayList<>();
        boardRepository.findByKeyWord(keyword).forEach(board -> responses.add(ReadBoardResponseDto.from(board)));

        return responses;
    }

    public List<ReadBoardResponseDto> getBoards() {
        List<ReadBoardResponseDto> responses = new ArrayList<>();
        boardRepository.findAll().forEach(board -> responses.add(ReadBoardResponseDto.from(board)));

        return responses;
    }

    @Transactional
    public void deleteBoard(Long boardId) {
        if (!boardRepository.existsById(boardId)) {
            throw new NotFoundException(ErrorCode.BOARD_NOT_FOUND);
        }

        List<Post> posts = postRepository.findByBoardId(boardId);
        posts.forEach(post -> {
            // post의 모든 comment 제거
            List<Comment> comments = commentRepository.findByPostId(post.getId());
            comments.forEach(comment -> comment.updateParentComment(null));

            commentRepository.deleteAll(comments);
        });
        // board의 모든 post 제거
        postRepository.deleteAll(posts);

        // board 제거
        boardRepository.deleteById(boardId);
    }
}
