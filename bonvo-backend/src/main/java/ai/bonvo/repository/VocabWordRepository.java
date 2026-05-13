package ai.bonvo.repository;

import ai.bonvo.entity.VocabWord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VocabWordRepository extends JpaRepository<VocabWord, Long> {

    List<VocabWord> findByTripIdAndUserId(Long tripId, Long userId);

    List<VocabWord> findByTripIdAndUserIdAndStatus(Long tripId, Long userId, VocabWord.WordStatus status);

    Optional<VocabWord> findByIdAndUserId(Long wordId, Long userId);

    long countByTripIdAndUserIdAndStatus(Long tripId, Long userId, VocabWord.WordStatus status);
}
