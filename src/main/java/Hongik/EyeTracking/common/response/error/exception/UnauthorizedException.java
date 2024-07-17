package Hongik.EyeTracking.common.response.error.exception;



import Hongik.EyeTracking.common.response.error.ErrorCode;
import lombok.Getter;


@Getter
public class UnauthorizedException extends RuntimeException {

    public UnauthorizedException(ErrorCode code) {
        super(code.getMessage());
    }
}
