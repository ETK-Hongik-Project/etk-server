package Hongik.EyeTracking.comment.repository;

import Hongik.EyeTracking.comment.domain.Comment;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Override
    @EntityGraph(attributePaths = {"commenter"})
    Optional<Comment> findById(Long commentId);

    @EntityGraph(attributePaths = {"commenter"})
    List<Comment> findByPostId(Long postId);

    List<Comment> findByCommenterId(Long userId);

    void deleteByPostId(Long postId);
}
