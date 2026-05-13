package ai.bonvo.controller;

import ai.bonvo.entity.User;
import ai.bonvo.repository.TripRepository;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final TripRepository tripRepository;

    /** GET /api/users/me */
    @GetMapping("/me")
    public ResponseEntity<ProfileResponse> getMe(@AuthenticationPrincipal User user) {
        long completedTrips = tripRepository.countCompletedByUserId(user.getId());
        String initials = buildInitials(user.getName());

        return ResponseEntity.ok(ProfileResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .avatarInitials(initials)
                .completedTrips(completedTrips)
                .build());
    }

    private String buildInitials(String name) {
        String[] parts = name.trim().split(" ");
        if (parts.length == 1) return parts[0].substring(0, 1).toUpperCase();
        return (parts[0].charAt(0) + "" + parts[parts.length - 1].charAt(0)).toUpperCase();
    }

    @Data @Builder
    public static class ProfileResponse {
        private Long id;
        private String name;
        private String email;
        private String avatarInitials;
        private long completedTrips;
    }
}
