package ai.bonvo.service;

import ai.bonvo.entity.Trip;
import ai.bonvo.entity.User;
import ai.bonvo.exception.ResourceNotFoundException;
import ai.bonvo.repository.TripRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AiService {

    private final ChatClient.Builder chatClientBuilder;
    private final TripRepository tripRepository;
    private final TripService tripService;

    /**
     * Streams an AI-generated travel plan via SSE.
     */
    public Flux<String> streamTravelPlan(Long tripId, User user) {
        Trip trip = tripRepository.findByIdAndUserId(tripId, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Gezi", tripId));

        String systemPrompt = """
                Sen Bonvo adlı bir AI seyahat asistanısın.
                Kullanıcıların seyahat planlarını günlük programlar, ipuçları ve yerel önerilerle zenginleştiriyorsun.
                Yanıtların Türkçe olmalı. Markdown formatını kullan.
                Her gün için: sabah, öğle ve akşam aktivitelerini listele.
                Yerel yemek, ulaşım ve para birimi ipuçları ekle.
                """;

        String userPrompt = buildTripPrompt(trip);

        ChatClient chatClient = chatClientBuilder.build();
        StringBuilder fullPlan = new StringBuilder();

        // Prompt nesnesini açıkça oluşturuyoruz
        return chatClient.prompt(new Prompt(List.of(
                        new SystemMessage(systemPrompt),
                        new UserMessage(userPrompt))))
                .stream()
                .content()
                .doOnNext(fullPlan::append)
                .doOnComplete(() -> {
                    log.info("AI plan completed for trip {}", tripId);
                    tripService.saveAiPlan(tripId, fullPlan.toString());
                })
                .doOnCancel(() -> {
                    // Opsiyonel: Kullanıcı bağlantıyı keserse yarıda kalan planı yine de kaydedebilirsin
                    log.warn("AI streaming cancelled for trip {}", tripId);
                })
                .doOnError(e -> log.error("AI streaming error for trip {}: {}", tripId, e.getMessage()));
    }

    /**
     * Generate vocabulary suggestions for a destination language.
     */
    public Flux<String> streamVocabSuggestions(Long tripId, User user) {
        Trip trip = tripRepository.findByIdAndUserId(tripId, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Gezi", tripId));

        String promptContent = String.format("""
                %s şehrine seyahat edecek bir Türk turist için
                en faydalı 15 yerel dil kelimesini veya ifadesini listele.
                Format: Kelime (orijinal yazılış) — Türkçe anlam — Kullanım örneği
                Yanıtı Türkçe ver.
                """, trip.getDestination());

        // HATA BURADAYDI: prompt() içine doğrudan String yerine new Prompt() veya .user() ekledik
        return chatClientBuilder.build()
                .prompt(new Prompt(promptContent))
                .stream()
                .content();
    }

    // ── Prompt builder ────────────────────────────────
    private String buildTripPrompt(Trip trip) {
        return String.format("""
                Seyahat planı oluştur:
                - Destinasyon: %s, %s
                - Tarihler: %s - %s
                - Gezi türü: %s
                - Bütçe: ₺%s (kişi başı)
                - Kişi sayısı: %d
                - Konaklama: %s
                - İlgi alanları: %s
                
                Lütfen günlük program, önerilen restoranlar, müzeler/aktiviteler,
                ulaşım ipuçları ve bütçe önerilerini içeren detaylı bir plan hazırla.
                """,
                trip.getDestination(),
                trip.getCountry() != null ? trip.getCountry() : "",
                trip.getStartDate(),
                trip.getEndDate(),
                trip.getType().name(),
                trip.getBudget().toPlainString(),
                trip.getTravelers(),
                trip.getAccommodation().name(),
                trip.getInterests() != null ? String.join(", ", trip.getInterests()) : ""
        );
    }
}