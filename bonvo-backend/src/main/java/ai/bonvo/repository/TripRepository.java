package ai.bonvo.repository;

import ai.bonvo.entity.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TripRepository extends JpaRepository<Trip, Long> {

    List<Trip> findByUserIdOrderByCreatedAtDesc(Long userId);

    Optional<Trip> findByIdAndUserId(Long tripId, Long userId);

    @Query("SELECT COUNT(t) FROM Trip t WHERE t.user.id = :userId AND t.status = 'COMPLETED'")
    long countCompletedByUserId(Long userId);
}
