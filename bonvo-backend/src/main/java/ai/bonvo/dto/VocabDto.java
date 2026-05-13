package ai.bonvo.dto;

import ai.bonvo.entity.VocabWord;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

public class VocabDto {

    @Data
    public static class CreateRequest {
        @NotBlank private String original;
        @NotBlank private String translation;
                  private String example;
    }

    @Data
    public static class UpdateStatusRequest {
        @NotNull private VocabWord.WordStatus status;
    }

    @Data @Builder
    public static class Response {
        private Long id;
        private Long tripId;
        private String original;
        private String translation;
        private String example;
        private VocabWord.WordStatus status;
        private LocalDateTime createdAt;
    }

    @Data @Builder
    public static class StatsResponse {
        private long learned;
        private long learning;
        private long hard;
        private long total;
    }
}
