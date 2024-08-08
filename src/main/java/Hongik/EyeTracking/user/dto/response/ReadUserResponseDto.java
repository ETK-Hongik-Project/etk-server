package Hongik.EyeTracking.user.dto.response;

import Hongik.EyeTracking.user.domain.User;
import jakarta.persistence.Column;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class ReadUserResponseDto {
    private Long id;
    private String name;
    private String username;
    private String email;

    public static ReadUserResponseDto from(User user) {
        return new ReadUserResponseDto(user.getId(), user.getName(), user.getUsername(), user.getEmail());
    }
}
