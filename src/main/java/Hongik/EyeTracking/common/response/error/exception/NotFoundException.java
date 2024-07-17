package Hongik.EyeTracking.common.response.error.exception;


import Hongik.EyeTracking.common.response.error.ErrorCode;
import lombok.Getter;


@Getter
public class NotFoundException extends RuntimeException {

    public NotFoundException(ErrorCode code) {
        super(code.getMessage());
    }
}
