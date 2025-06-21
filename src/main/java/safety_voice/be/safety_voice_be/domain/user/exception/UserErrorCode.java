package safety_voice.be.safety_voice_be.domain.user.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import safety_voice.be.safety_voice_be.global.exception.response.BaseErrorCode;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements BaseErrorCode {
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "404_001", "유저 정보를 찾을 수 없습니다."),
    SETTING_NOT_FOUND(HttpStatus.NOT_FOUND, "404_002", "사용자 설정 정보를 찾을 수 없습니다."),
    INVALID_TRIGGER_VALUE(HttpStatus.BAD_REQUEST, "400_001", "트리거 관련 설정 값이 잘못되었습니다."),
    INVALID_EMERGENCY_VALUE(HttpStatus.BAD_REQUEST, "400_002", "비상 연락 관련 설정 값이 잘못되었습니다."),
    EMPTY_EMERGENCY_CONTACTS(HttpStatus.BAD_REQUEST, "400_003", "비상 연락망은 최소 1명 이상이어야 합니다."),
    VOICE_SAMPLE_URL_INVALID(HttpStatus.BAD_REQUEST, "400_004", "음성 샘플 URL이 유효하지 않습니다."),
    SETTING_ALREADY_EXISTS(HttpStatus.CONFLICT, "409_001", "이미 해당 유저의 설정이 존재합니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

}
