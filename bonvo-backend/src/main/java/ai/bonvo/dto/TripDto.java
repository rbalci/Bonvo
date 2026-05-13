package ai.bonvo.dto;

import ai.bonvo.entity.Trip;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class TripDto {

    @Data
    public static class CreateRequest {
        @NotBlank  private String destination;
                   private String country;
                   private String flag;
        @NotNull   private LocalDate startDate;
        @NotNull   private LocalDate endDate;
        @NotNull   private Trip.TripType type;
        @NotNull @Positive private BigDecimal budget;
        @NotNull   private Integer travelers;
        @NotNull   private Trip.Accommodation accommodation;
                   private List<String> interests;
    }

    @Data @Builder
    public static class Response {
        private Long id;
        private String destination;
        private String country;
        private String flag;
        private LocalDate startDate;
        private LocalDate endDate;
        private Trip.TripType type;
        private BigDecimal budget;
        private Integer travelers;
        private Trip.Accommodation accommodation;
        private List<String> interests;
        private Trip.TripStatus status;
        private int progress;
        private Long daysLeft;
        private String aiPlan;
        private LocalDateTime createdAt;
    }
}
