package Hongik.EyeTracking.user.service;

import Hongik.EyeTracking.comment.repository.CommentRepository;
import Hongik.EyeTracking.common.response.error.ErrorCode;
import Hongik.EyeTracking.common.response.error.exception.DuplicateException;
import Hongik.EyeTracking.common.response.error.exception.NotFoundException;
import Hongik.EyeTracking.image.domain.Image;
import Hongik.EyeTracking.image.repository.ImageRepository;
import Hongik.EyeTracking.post.repository.PostRepository;
import Hongik.EyeTracking.user.domain.Role;
import Hongik.EyeTracking.user.domain.User;
import Hongik.EyeTracking.user.dto.request.CreateUserRequestDto;
import Hongik.EyeTracking.user.dto.response.CreateUserResponseDto;
import Hongik.EyeTracking.user.dto.response.ReadUserResponseDto;
import Hongik.EyeTracking.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Transactional
    public CreateUserResponseDto createUser(CreateUserRequestDto requestDto) {
        if (userRepository.existsByUsername(requestDto.getUsername())) {
            throw new DuplicateException(ErrorCode.USERNAME_ALREADY_EXISTS);
        }
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new DuplicateException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }

        User user = User.builder()
                .name(requestDto.getName())
                .username(requestDto.getUsername())
                .password(passwordEncoder.encode(requestDto.getPassword()))
                .email(requestDto.getEmail())
                .role(Role.USER)
                .build();

        userRepository.save(user);

        return CreateUserResponseDto.from(user);
    }

    public ReadUserResponseDto getUser(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new NotFoundException(ErrorCode.USER_NOT_FOUND)
        );

        return ReadUserResponseDto.from(user);
    }

    @Transactional
    public void deleteUser(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() ->
            new NotFoundException(ErrorCode.USER_NOT_FOUND)
        );

        Long userId = user.getId();

        // 이미지 제거
        List<Image> images = imageRepository.findByUserId(userId);
        imageRepository.deleteAll(images);

        // comment 연관관계 해제
        commentRepository.findByCommenterId(userId).forEach(comment -> {
            comment.updateCommenter(null);
        });

        // Post 연관관계 해제
        postRepository.findByAuthorId(userId).forEach(post -> {
            post.updateAuthor(null);
        });

        userRepository.delete(user);
    }
}
