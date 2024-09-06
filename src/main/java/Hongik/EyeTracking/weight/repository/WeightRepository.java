package Hongik.EyeTracking.weight.repository;

import Hongik.EyeTracking.weight.domain.Weight;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WeightRepository extends JpaRepository<Weight, Long> {
    Optional<Weight> findByUserId(Long userId);
}
