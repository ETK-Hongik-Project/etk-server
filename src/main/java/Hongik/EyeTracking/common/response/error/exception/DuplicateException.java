package Hongik.EyeTracking.common.response.error.exception;



import Hongik.EyeTracking.common.response.error.ErrorCode;
import lombok.Getter;


@Getter
public class DuplicateException extends RuntimeException {

    public DuplicateException(ErrorCode code) {
        super(code.getMessage());
    }
}
