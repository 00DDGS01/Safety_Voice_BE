package safety_voice.be.safety_voice_be.domain.safe_zone.dto;

import jakarta.persistence.Column;
import lombok.*;

import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SafeZoneRequestDto {
    private String safeZoneName;
    private Integer radius;
    private Double latitude;
    private Double longitude;
    private List<SafeTimeDto> safeTimes;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class SafeTimeDto {
        private LocalTime startTime;
        private LocalTime endTime;
        private String daysActive; // "MON,TUE,WED"
    }
}
