package ai.bonvo.controller;

import ai.bonvo.entity.User;
import ai.bonvo.service.AiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiController {

    private final AiService aiService;

    /**
     * GET /api/ai/trips/{tripId}/plan
     * Returns a Server-Sent Events stream of the AI-generated travel plan.
     * Frontend subscribes with: new EventSource('/api/ai/trips/1/plan', {headers: {Authorization: 'Bearer ...'}})
     */
    @GetMapping(value = "/trips/{tripId}/plan", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamTravelPlan(
            @PathVariable Long tripId,
            @AuthenticationPrincipal User user) {
        return aiService.streamTravelPlan(tripId, user);
    }

    /**
     * GET /api/ai/trips/{tripId}/vocab-suggestions
     * Streams AI-suggested vocabulary words for the trip destination.
     */
    @GetMapping(value = "/trips/{tripId}/vocab-suggestions", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamVocabSuggestions(
            @PathVariable Long tripId,
            @AuthenticationPrincipal User user) {
        return aiService.streamVocabSuggestions(tripId, user);
    }
}
