package ai.bonvo.controller;

import ai.bonvo.dto.TripDto;
import ai.bonvo.entity.Trip;
import ai.bonvo.entity.User;
import ai.bonvo.service.TripService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/trips")
@RequiredArgsConstructor
public class TripController {

    private final TripService tripService;

    /** GET /api/trips — benim tüm gezilerim */
    @GetMapping
    public ResponseEntity<List<TripDto.Response>> getMyTrips(
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(tripService.getMyTrips(user));
    }

    /** GET /api/trips/{id} */
    @GetMapping("/{id}")
    public ResponseEntity<TripDto.Response> getTrip(
            @PathVariable Long id,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(tripService.getTrip(id, user));
    }

    /** POST /api/trips */
    @PostMapping
    public ResponseEntity<TripDto.Response> createTrip(
            @Valid @RequestBody TripDto.CreateRequest request,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(tripService.createTrip(request, user));
    }

    /** PATCH /api/trips/{id}/status */
    @PatchMapping("/{id}/status")
    public ResponseEntity<TripDto.Response> updateStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> body,
            @AuthenticationPrincipal User user) {
        Trip.TripStatus status = Trip.TripStatus.valueOf(body.get("status").toUpperCase());
        return ResponseEntity.ok(tripService.updateStatus(id, status, user));
    }

    /** DELETE /api/trips/{id} */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrip(
            @PathVariable Long id,
            @AuthenticationPrincipal User user) {
        tripService.deleteTrip(id, user);
        return ResponseEntity.noContent().build();
    }
}
