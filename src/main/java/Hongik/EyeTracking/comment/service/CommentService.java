package Hongik.EyeTracking.comment.service;

import Hongik.EyeTracking.comment.domain.Comment;
import Hongik.EyeTracking.comment.dto.request.CreateCommentRequestDto;
import Hongik.EyeTracking.comment.dto.response.CreateCommentResponseDto;
import Hongik.EyeTracking.comment.dto.response.ReadCommentResponseDto;
import Hongik.EyeTracking.comment.repository.CommentRepository;
import Hongik.EyeTracking.common.response.error.ErrorCode;
import Hongik.EyeTracking.common.response.error.exception.BadRequestException;
import Hongik.EyeTracking.common.response.error.exception.NotFoundException;
import Hongik.EyeTracking.post.domain.Post;
import Hongik.EyeTracking.post.repository.PostRepository;
import Hongik.EyeTracking.user.domain.User;
import Hongik.EyeTracking.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public CreateCommentResponseDto createComment(String username, Long postId, CreateCommentRequestDto requestDto) {
        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new NotFoundException(ErrorCode.USER_NOT_FOUND)
        );

        Post post = postRepository.findById(postId).orElseThrow(() ->
                new NotFoundException(ErrorCode.POST_NOT_FOUND)
        );

        Comment comment = Comment.builder()
                .commenter(user)
                .post(post)
                .parentComment(null)
                .content(requestDto.getContent())
                .build();

        commentRepository.save(comment);

        return CreateCommentResponseDto.from(comment);
    }

    @Transactional
    public CreateCommentResponseDto createReply(String username, Long commentId, CreateCommentRequestDto requestDto) {
        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new NotFoundException(ErrorCode.USER_NOT_FOUND)
        );

        Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
                new NotFoundException(ErrorCode.COMMENT_NOT_FOUND)
        );

        Comment reply = Comment.builder()
                .commenter(user)
                .post(comment.getPost())
                .parentComment(comment)
                .content(requestDto.getContent())
                .build();

        commentRepository.save(reply);

        return CreateCommentResponseDto.from(reply);
    }

    public List<ReadCommentResponseDto> readCommentsOfPost(Long postId) {
        if (!postRepository.existsById(postId)) {
            throw new NotFoundException(ErrorCode.POST_NOT_FOUND);
        }

        List<ReadCommentResponseDto> responses = new ArrayList<>();
        // 대댓글이 아닌 댓글만 조회
        commentRepository.findByPostIdAndParentCommentId(postId, null).forEach(comment -> {
            // 대댓글 조회
            List<Comment> replies = commentRepository.findByParentCommentId(comment.getId());
            responses.add(ReadCommentResponseDto.of(comment, replies));
        });

        return responses;
    }

    @Transactional
    public void deleteComment(String username, Long commentId) {
        if (!userRepository.existsByUsername(username)) {
            throw new NotFoundException(ErrorCode.USER_NOT_FOUND);
        }

        Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
                new NotFoundException(ErrorCode.COMMENT_NOT_FOUND)
        );

        if (!comment.getCommenter().getUsername().equals(username)) {
            throw new BadRequestException(ErrorCode.NOT_USER_COMMENT);
        }

        comment.updateCommenter(null);
        comment.updateContent("삭제된 댓글입니다.");
    }
}
