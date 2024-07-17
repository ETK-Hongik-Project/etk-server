package Hongik.EyeTracking.common.response.error.exception;



import Hongik.EyeTracking.common.response.error.ErrorCode;
import lombok.Getter;


@Getter
public class InterruptedException extends RuntimeException {

    public InterruptedException(ErrorCode code) {
        super(code.getMessage());
    }
}
