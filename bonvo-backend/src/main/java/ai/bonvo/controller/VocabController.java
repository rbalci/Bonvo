package ai.bonvo.controller;

import ai.bonvo.dto.VocabDto;
import ai.bonvo.entity.User;
import ai.bonvo.entity.VocabWord;
import ai.bonvo.service.VocabService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/trips/{tripId}/vocab")
@RequiredArgsConstructor
public class VocabController {

    private final VocabService vocabService;

    /** GET /api/trips/{tripId}/vocab?status=LEARNING */
    @GetMapping
    public ResponseEntity<List<VocabDto.Response>> getWords(
            @PathVariable Long tripId,
            @RequestParam(required = false) VocabWord.WordStatus status,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(vocabService.getWords(tripId, user, status));
    }

    /** GET /api/trips/{tripId}/vocab/stats */
    @GetMapping("/stats")
    public ResponseEntity<VocabDto.StatsResponse> getStats(
            @PathVariable Long tripId,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(vocabService.getStats(tripId, user));
    }

    /** POST /api/trips/{tripId}/vocab */
    @PostMapping
    public ResponseEntity<VocabDto.Response> addWord(
            @PathVariable Long tripId,
            @Valid @RequestBody VocabDto.CreateRequest request,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(vocabService.addWord(tripId, request, user));
    }

    /** PATCH /api/trips/{tripId}/vocab/{wordId}/status */
    @PatchMapping("/{wordId}/status")
    public ResponseEntity<VocabDto.Response> updateStatus(
            @PathVariable Long tripId,
            @PathVariable Long wordId,
            @RequestBody Map<String, String> body,
            @AuthenticationPrincipal User user) {
        VocabWord.WordStatus status = VocabWord.WordStatus.valueOf(body.get("status").toUpperCase());
        return ResponseEntity.ok(vocabService.updateStatus(wordId, status, user));
    }

    /** DELETE /api/trips/{tripId}/vocab/{wordId} */
    @DeleteMapping("/{wordId}")
    public ResponseEntity<Void> deleteWord(
            @PathVariable Long tripId,
            @PathVariable Long wordId,
            @AuthenticationPrincipal User user) {
        vocabService.deleteWord(wordId, user);
        return ResponseEntity.noContent().build();
    }
}
