package Hongik.EyeTracking.common.response.error.exception;


import Hongik.EyeTracking.common.response.error.ErrorCode;
import lombok.Getter;


@Getter
public class NoSuchElementException extends RuntimeException {

    public NoSuchElementException(ErrorCode code) {
        super(code.getMessage());
    }
}
