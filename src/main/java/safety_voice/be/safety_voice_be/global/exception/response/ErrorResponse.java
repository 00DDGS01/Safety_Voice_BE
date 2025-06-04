package safety_voice.be.safety_voice_be.global.exception.response;

import lombok.Builder;
import lombok.Getter;
import safety_voice.be.safety_voice_be.global.exception.code.ErrorCode;

@Getter
@Builder
public class ErrorResponse {
    private final int status;
    private final String code;
    private final String message;

    public static ErrorResponse from(ErrorCode errorCode) {
        return ErrorResponse.builder()
                .status(errorCode.getHttpStatus().value())
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .build();
    }
}
