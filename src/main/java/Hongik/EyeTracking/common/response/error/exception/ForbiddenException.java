package Hongik.EyeTracking.common.response.error.exception;



import Hongik.EyeTracking.common.response.error.ErrorCode;
import lombok.Getter;


@Getter
public class ForbiddenException extends RuntimeException {

    public ForbiddenException(ErrorCode code) {
        super(code.getMessage());
    }
}
