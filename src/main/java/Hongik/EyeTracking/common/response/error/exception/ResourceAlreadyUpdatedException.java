package Hongik.EyeTracking.common.response.error.exception;

import lombok.Getter;

@Getter
public class ResourceAlreadyUpdatedException extends RuntimeException {
    public ResourceAlreadyUpdatedException(String message) {
        super(message);
    }
}
