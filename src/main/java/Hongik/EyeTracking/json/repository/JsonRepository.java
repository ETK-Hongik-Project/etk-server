package Hongik.EyeTracking.json.repository;

import Hongik.EyeTracking.json.domain.Json;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JsonRepository extends JpaRepository<Json, Long> {
    Optional<Json> findById(Long jsonId);
    Optional<Json> findByUserIdAndId(Long userId, Long jsonId);

    List<Json> findByUserId(Long userId);

    void deleteById(Long jsonId);

    void deleteAllByUserId(Long userId);
}
