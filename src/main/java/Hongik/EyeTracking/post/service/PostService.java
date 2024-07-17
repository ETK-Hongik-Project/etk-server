package Hongik.EyeTracking.post.service;

import Hongik.EyeTracking.board.domain.Board;
import Hongik.EyeTracking.board.repository.BoardRepository;
import Hongik.EyeTracking.common.response.error.ErrorCode;
import Hongik.EyeTracking.common.response.error.exception.NotFoundException;
import Hongik.EyeTracking.post.domain.Post;
import Hongik.EyeTracking.post.dto.request.CreatePostRequestDto;
import Hongik.EyeTracking.post.dto.response.ReadPostResponseDto;
import Hongik.EyeTracking.post.repository.PostRepository;
import Hongik.EyeTracking.user.domain.User;
import Hongik.EyeTracking.user.repository.UserRepository;
import jakarta.transaction.TransactionScoped;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private static final int PAGE_SIZE = 10;

    @Transactional
    public Long createPost(String username, Long boardId, CreatePostRequestDto requestDto) {
        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new NotFoundException(ErrorCode.USER_NOT_FOUND)
        );

        Board board = boardRepository.findById(boardId).orElseThrow(() ->
                new NotFoundException(ErrorCode.BOARD_NOT_FOUND)
        );

        Post post = Post.builder()
                .author(user)
                .board(board)
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .isAnonymous(requestDto.getIsAnonymous())
                .build();

        postRepository.save(post);

        return post.getId();
    }

    public ReadPostResponseDto getPost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() ->
                new NotFoundException(ErrorCode.POST_NOT_FOUND)
        );

        return ReadPostResponseDto.from(post);
    }

    public List<ReadPostResponseDto> getPostsOfUser(String username, int pageNo) {
        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new NotFoundException(ErrorCode.USER_NOT_FOUND)
        );

        List<ReadPostResponseDto> responses = new ArrayList<>();

        Pageable pageable = PageRequest.of(pageNo, PAGE_SIZE, Sort.Direction.DESC, "createdDate");
        postRepository.findByAuthorId(user.getId(), pageable)
                .forEach(post -> responses.add(ReadPostResponseDto.of(post, user.getName())));

        return responses;
    }

    public List<ReadPostResponseDto> getPostsOfBoard(Long boardId, int pageNo) {
        if (boardRepository.existsById(boardId)) {
            throw new NotFoundException(ErrorCode.BOARD_NOT_FOUND);
        }

        List<ReadPostResponseDto> responses = new ArrayList<>();

        Pageable pageable = PageRequest.of(pageNo, PAGE_SIZE, Sort.Direction.DESC, "createdDate");
        postRepository.findByBoardId(boardId, pageable).forEach(post ->
                responses.add(ReadPostResponseDto.from(post))
        );

        return responses;
    }

    @Transactional
    public void deletePost(Long postId) {
        if (!postRepository.existsById(postId)) {
            throw new NotFoundException(ErrorCode.POST_NOT_FOUND);
        }

        postRepository.deleteById(postId);
    }
}
