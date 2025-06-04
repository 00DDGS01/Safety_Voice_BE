package safety_voice.be.safety_voice_be.global.exception.response;

public class CustomException extends RuntimeException {
    private final BaseErrorCode errorCode;


    public CustomException(BaseErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public CustomException(BaseErrorCode errorCode, String message) {
        super(errorCode.getMessage() + ": " + message);
        this.errorCode = errorCode;
    }

    public BaseErrorCode getErrorCode() {
        return errorCode;
    }
}
