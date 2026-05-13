package ai.bonvo.controller;

import ai.bonvo.dto.FlightDto;
import ai.bonvo.service.FlightService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/flights")
@RequiredArgsConstructor
public class FlightController {

    private final FlightService flightService;

    /** POST /api/flights/search */
    @PostMapping("/search")
    public ResponseEntity<FlightDto.SearchResponse> searchFlights(
            @Valid @RequestBody FlightDto.SearchRequest request) {
        return ResponseEntity.ok(flightService.searchFlights(request));
    }
}
