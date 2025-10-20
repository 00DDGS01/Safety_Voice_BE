package safety_voice.be.safety_voice_be.domain.safe_zone.dto;

import lombok.*;

import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SafeZoneResponseDto {
    private Long id;
    private String safeZoneName;
    private String location;
    private Double latitude;
    private Double longitude;
    private Integer radius;
    private List<SafeTimeDto> safeTimes;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class SafeTimeDto {
        private Long id;
        private LocalTime startTime;
        private LocalTime endTime;
        private String daysActive;
    }
}