package safety_voice.be.safety_voice_be.domain.sms.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EmergencySmsRequestDto {
    private double latitude;
    private double longitude;

    public String buildMessage() {
        return String.format(
                "[긴급 알림 - 안전한 목소리]\n%s님이 위험 신호를 보냈습니다.\n위치: https://maps.google.com/?q=%f,%f\n즉시 확인 부탁드립니다.",

                latitude,
                longitude
        );
    }
}