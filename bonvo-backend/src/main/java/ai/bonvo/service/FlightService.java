package ai.bonvo.service;

import ai.bonvo.dto.FlightDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class FlightService {

    @Qualifier("amadeusWebClient")
    private final WebClient amadeusWebClient;

    @Value("${bonvo.amadeus.client-id}")
    private String clientId;

    @Value("${bonvo.amadeus.client-secret}")
    private String clientSecret;

    /**
     * Searches for flights via Amadeus API.
     * Results cached for 10 minutes (see application.yml caffeine spec).
     */
    @Cacheable(value = "flights", key = "#req.originCode + '-' + #req.destinationCode + '-' + #req.departureDate")
    public FlightDto.SearchResponse searchFlights(FlightDto.SearchRequest req) {
        log.info("Searching flights: {} -> {} on {}", req.getOriginCode(), req.getDestinationCode(), req.getDepartureDate());

        // In a real integration, first fetch an access token, then call the API.
        // Here we return mock data so the service compiles and runs without API keys.
        return mockFlightResponse(req);
    }

    // ── Amadeus OAuth2 token fetch (real implementation) ──
    @SuppressWarnings("unused")
    private Mono<String> fetchAmadeusToken() {
        return amadeusWebClient.post()
                .uri("/v1/security/oauth2/token")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .bodyValue("grant_type=client_credentials&client_id=" + clientId
                        + "&client_secret=" + clientSecret)
                .retrieve()
                .bodyToMono(Map.class)
                .map(body -> (String) body.get("access_token"));
    }

    // ── Mock response (replace with real Amadeus call) ───
    private FlightDto.SearchResponse mockFlightResponse(FlightDto.SearchRequest req) {
        List<FlightDto.Offer> offers = List.of(
                FlightDto.Offer.builder()
                        .id("FL001")
                        .airline("Turkish Airlines")
                        .departureTime("10:30")
                        .arrivalTime("21:45")
                        .duration("13s 15dk")
                        .stops(0)
                        .price(new BigDecimal("12500"))
                        .currency("TRY")
                        .bookingUrl("https://turkishairlines.com")
                        .build(),
                FlightDto.Offer.builder()
                        .id("FL002")
                        .airline("Qatar Airways")
                        .departureTime("02:15")
                        .arrivalTime("19:00")
                        .duration("18s 45dk")
                        .stops(1)
                        .price(new BigDecimal("9800"))
                        .currency("TRY")
                        .bookingUrl("https://qatarairways.com")
                        .build()
        );

        return FlightDto.SearchResponse.builder()
                .offers(offers)
                .total(offers.size())
                .currency("TRY")
                .build();
    }
}
