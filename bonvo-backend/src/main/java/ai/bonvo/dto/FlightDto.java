package ai.bonvo.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class FlightDto {

    @Data
    public static class SearchRequest {
        @NotBlank private String originCode;      // IATA: IST
        @NotBlank private String destinationCode; // IATA: TYO
        @NotNull  private LocalDate departureDate;
                  private LocalDate returnDate;
        @Min(1)   private int adults = 1;
                  private String cabinClass = "ECONOMY";
    }

    @Data @Builder
    public static class Offer {
        private String id;
        private String airline;
        private String airlineLogo;
        private String departureTime;
        private String arrivalTime;
        private String duration;
        private int stops;
        private BigDecimal price;
        private String currency;
        private String bookingUrl;
    }

    @Data @Builder
    public static class SearchResponse {
        private List<Offer> offers;
        private int total;
        private String currency;
    }
}
