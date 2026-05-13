package ai.bonvo.service;

import ai.bonvo.dto.TripDto;
import ai.bonvo.entity.Trip;
import ai.bonvo.entity.User;
import ai.bonvo.exception.ResourceNotFoundException;
import ai.bonvo.repository.TripRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TripService {

    private final TripRepository tripRepository;

    // ── List ──────────────────────────────────────────
    public List<TripDto.Response> getMyTrips(User user) {
        return tripRepository.findByUserIdOrderByCreatedAtDesc(user.getId())
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // ── Get one ───────────────────────────────────────
    public TripDto.Response getTrip(Long tripId, User user) {
        Trip trip = findOwned(tripId, user);
        return toResponse(trip);
    }

    // ── Create ────────────────────────────────────────
    @Transactional
    public TripDto.Response createTrip(TripDto.CreateRequest req, User user) {
        Trip trip = Trip.builder()
                .user(user)
                .destination(req.getDestination())
                .country(req.getCountry())
                .flag(req.getFlag())
                .startDate(req.getStartDate())
                .endDate(req.getEndDate())
                .type(req.getType())
                .budget(req.getBudget())
                .travelers(req.getTravelers())
                .accommodation(req.getAccommodation())
                .interests(req.getInterests() != null ? req.getInterests() : List.of())
                .build();

        return toResponse(tripRepository.save(trip));
    }

    // ── Update status ─────────────────────────────────
    @Transactional
    public TripDto.Response updateStatus(Long tripId, Trip.TripStatus status, User user) {
        Trip trip = findOwned(tripId, user);
        trip.setStatus(status);
        return toResponse(tripRepository.save(trip));
    }

    // ── Delete ────────────────────────────────────────
    @Transactional
    public void deleteTrip(Long tripId, User user) {
        Trip trip = findOwned(tripId, user);
        tripRepository.delete(trip);
    }

    // ── Update AI plan (called by AiService) ──────────
    @Transactional
    public void saveAiPlan(Long tripId, String plan) {
        tripRepository.findById(tripId).ifPresent(t -> {
            t.setAiPlan(plan);
            tripRepository.save(t);
        });
    }

    // ── Helpers ───────────────────────────────────────
    private Trip findOwned(Long tripId, User user) {
        return tripRepository.findByIdAndUserId(tripId, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Gezi", tripId));
    }

    private TripDto.Response toResponse(Trip trip) {
        long daysLeft = trip.getStartDate() != null
                ? Math.max(0, ChronoUnit.DAYS.between(LocalDate.now(), trip.getStartDate()))
                : 0;

        int progress = calculateProgress(trip);

        return TripDto.Response.builder()
                .id(trip.getId())
                .destination(trip.getDestination())
                .country(trip.getCountry())
                .flag(trip.getFlag())
                .startDate(trip.getStartDate())
                .endDate(trip.getEndDate())
                .type(trip.getType())
                .budget(trip.getBudget())
                .travelers(trip.getTravelers())
                .accommodation(trip.getAccommodation())
                .interests(trip.getInterests())
                .status(trip.getStatus())
                .progress(progress)
                .daysLeft(daysLeft)
                .aiPlan(trip.getAiPlan())
                .createdAt(trip.getCreatedAt())
                .build();
    }

    private int calculateProgress(Trip trip) {
        if (trip.getStatus() == Trip.TripStatus.COMPLETED) return 100;
        if (trip.getStatus() == Trip.TripStatus.ONGOING) return 50;
        boolean hasAiPlan = trip.getAiPlan() != null;
        return hasAiPlan ? 65 : 20;
    }
}
