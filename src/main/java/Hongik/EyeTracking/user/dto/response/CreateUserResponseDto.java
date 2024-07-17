package Hongik.EyeTracking.user.dto.response;

import Hongik.EyeTracking.user.domain.User;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class CreateUserResponseDto {
    private Long userId;
    private String name;

    public static CreateUserResponseDto from(User user) {
        return new CreateUserResponseDto(user.getId(), user.getName());
    }
}
