package Hongik.EyeTracking.post.repository;

import Hongik.EyeTracking.post.domain.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    @EntityGraph(attributePaths = {"author"})
    Optional<Post> findById(Long postId);

    List<Post> findByAuthorId(Long userId);

    List<Post> findByAuthorId(Long userId, Pageable pageable);

    List<Post> findByBoardId(Long boardId);

    @EntityGraph(attributePaths = {"author"})
    List<Post> findByBoardId(Long boardId, Pageable pageable);

    @Query("select p from Post p " +
            "where p.title like %:keyword% " +
            "and p.board.id = :boardId")
    @EntityGraph(attributePaths = {"author"})
    List<Post> findByBoardIdAndKeyword(@Param("boardId") Long boardId, @Param("keyword") String keyword, Pageable pageable);
}
