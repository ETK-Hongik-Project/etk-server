package Hongik.EyeTracking.board.repository;

import Hongik.EyeTracking.board.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {
    @Query("select b from Board b where b.name like %:keyword%")
    List<Board> findByKeyWord(@Param("keyword")String keyword);

    boolean existsById(Long boardId);

    boolean existsByName(String name);
}
