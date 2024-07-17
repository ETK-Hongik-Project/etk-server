package Hongik.EyeTracking.common.response.error.exception;



import Hongik.EyeTracking.common.response.error.ErrorCode;
import lombok.Getter;


@Getter
public class BadRequestException extends RuntimeException {

    public BadRequestException(ErrorCode code) {
        super(code.getMessage());
    }
}
