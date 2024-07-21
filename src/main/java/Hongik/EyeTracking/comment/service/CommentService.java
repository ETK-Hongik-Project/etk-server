package Hongik.EyeTracking.comment.service;

import Hongik.EyeTracking.comment.domain.Comment;
import Hongik.EyeTracking.comment.dto.request.CreateCommentRequestDto;
import Hongik.EyeTracking.comment.dto.response.CreateCommentResponseDto;
import Hongik.EyeTracking.comment.repository.CommentRepository;
import Hongik.EyeTracking.common.response.error.ErrorCode;
import Hongik.EyeTracking.common.response.error.exception.NotFoundException;
import Hongik.EyeTracking.post.domain.Post;
import Hongik.EyeTracking.post.repository.PostRepository;
import Hongik.EyeTracking.user.domain.User;
import Hongik.EyeTracking.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
