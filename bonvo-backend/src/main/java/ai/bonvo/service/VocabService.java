package ai.bonvo.service;

import ai.bonvo.dto.VocabDto;
import ai.bonvo.entity.Trip;
import ai.bonvo.entity.User;
import ai.bonvo.entity.VocabWord;
import ai.bonvo.exception.ResourceNotFoundException;
import ai.bonvo.repository.TripRepository;
import ai.bonvo.repository.VocabWordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VocabService {

    private final VocabWordRepository vocabWordRepository;
    private final TripRepository tripRepository;

    // ── List words for a trip ─────────────────────────
    public List<VocabDto.Response> getWords(Long tripId, User user,
                                            VocabWord.WordStatus status) {
        List<VocabWord> words = status != null
                ? vocabWordRepository.findByTripIdAndUserIdAndStatus(tripId, user.getId(), status)
                : vocabWordRepository.findByTripIdAndUserId(tripId, user.getId());

        return words.stream().map(this::toResponse).toList();
    }

    // ── Stats ─────────────────────────────────────────
    public VocabDto.StatsResponse getStats(Long tripId, User user) {
        long learned  = vocabWordRepository.countByTripIdAndUserIdAndStatus(tripId, user.getId(), VocabWord.WordStatus.LEARNED);
        long learning = vocabWordRepository.countByTripIdAndUserIdAndStatus(tripId, user.getId(), VocabWord.WordStatus.LEARNING);
        long hard     = vocabWordRepository.countByTripIdAndUserIdAndStatus(tripId, user.getId(), VocabWord.WordStatus.HARD);

        return VocabDto.StatsResponse.builder()
                .learned(learned)
                .learning(learning)
                .hard(hard)
                .total(learned + learning + hard)
                .build();
    }

    // ── Add word ──────────────────────────────────────
    @Transactional
    public VocabDto.Response addWord(Long tripId, VocabDto.CreateRequest req, User user) {
        Trip trip = tripRepository.findByIdAndUserId(tripId, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Gezi", tripId));

        VocabWord word = VocabWord.builder()
                .trip(trip)
                .user(user)
                .original(req.getOriginal())
                .translation(req.getTranslation())
                .example(req.getExample())
                .build();

        return toResponse(vocabWordRepository.save(word));
    }

    // ── Update status ─────────────────────────────────
    @Transactional
    public VocabDto.Response updateStatus(Long wordId, VocabWord.WordStatus status, User user) {
        VocabWord word = vocabWordRepository.findByIdAndUserId(wordId, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Kelime", wordId));
        word.setStatus(status);
        return toResponse(vocabWordRepository.save(word));
    }

    // ── Delete ────────────────────────────────────────
    @Transactional
    public void deleteWord(Long wordId, User user) {
        VocabWord word = vocabWordRepository.findByIdAndUserId(wordId, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Kelime", wordId));
        vocabWordRepository.delete(word);
    }

    private VocabDto.Response toResponse(VocabWord w) {
        return VocabDto.Response.builder()
                .id(w.getId())
                .tripId(w.getTrip().getId())
                .original(w.getOriginal())
                .translation(w.getTranslation())
                .example(w.getExample())
                .status(w.getStatus())
                .createdAt(w.getCreatedAt())
                .build();
    }
}
