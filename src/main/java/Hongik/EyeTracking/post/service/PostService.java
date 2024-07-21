package Hongik.EyeTracking.post.service;

import Hongik.EyeTracking.board.domain.Board;
import Hongik.EyeTracking.board.repository.BoardRepository;
import Hongik.EyeTracking.comment.repository.CommentRepository;
import Hongik.EyeTracking.common.response.error.ErrorCode;
import Hongik.EyeTracking.common.response.error.exception.BadRequestException;
import Hongik.EyeTracking.common.response.error.exception.NotFoundException;
import Hongik.EyeTracking.post.domain.Post;
import Hongik.EyeTracking.post.dto.request.CreatePostRequestDto;
import Hongik.EyeTracking.post.dto.request.ModifyPostRequestDto;
import Hongik.EyeTracking.post.dto.response.CreatePostResponseDto;
import Hongik.EyeTracking.post.dto.response.ModifyPostResponseDto;
import Hongik.EyeTracking.post.dto.response.ReadPostResponseDto;
import Hongik.EyeTracking.post.repository.PostRepository;
import Hongik.EyeTracking.user.domain.User;
import Hongik.EyeTracking.user.repository.UserRepository;
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
    private final CommentRepository commentRepository;

    @Transactional
    public CreatePostResponseDto createPost(String username, Long boardId, CreatePostRequestDto requestDto) {
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

        return CreatePostResponseDto.from(post);
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

    public List<ReadPostResponseDto> getPostsOfBoard(Long boardId, int pageNo, String keyword) {
        if (boardRepository.existsById(boardId)) {
            throw new NotFoundException(ErrorCode.BOARD_NOT_FOUND);
        }

        List<ReadPostResponseDto> responses = new ArrayList<>();

        Pageable pageable = PageRequest.of(pageNo, PAGE_SIZE, Sort.Direction.DESC, "createdDate");
        postRepository.findByBoardIdAndKeyword(boardId, keyword, pageable).forEach(post ->
                responses.add(ReadPostResponseDto.from(post))
        );

        return responses;
    }

    @Transactional
    public ModifyPostResponseDto updatePost(String username, Long postId, ModifyPostRequestDto requestDto) {
        if (!userRepository.existsByUsername(username)) {
            throw new NotFoundException(ErrorCode.USER_NOT_FOUND);
        }

        Post post = postRepository.findById(postId).orElseThrow(() ->
                new NotFoundException(ErrorCode.POST_NOT_FOUND)
        );

        if (post.getAuthor().getUsername().equals(username)) {
            throw new BadRequestException(ErrorCode.NOT_USER_POST);
        }

        post.updateContent(requestDto.getContent());

        return ModifyPostResponseDto.from(post);
    }

    @Transactional
    public void deletePost(String username, Long postId) {
        if (!userRepository.existsByUsername(username)) {
            throw new NotFoundException(ErrorCode.USER_NOT_FOUND);
        }

        Post post = postRepository.findById(postId).orElseThrow(() ->
                new NotFoundException(ErrorCode.POST_NOT_FOUND)
        );

        if (!post.getAuthor().getUsername().equals(username)) {
            throw new BadRequestException(ErrorCode.NOT_USER_POST);
        }

        // 연관관계 제거
        commentRepository.findByPostId(postId).forEach(comment ->
                comment.updateParentComment(null)
        );

        // post의 comment들 제거
        commentRepository.deleteByPostId(postId);

        // post 제거
        postRepository.deleteById(postId);
    }
}
