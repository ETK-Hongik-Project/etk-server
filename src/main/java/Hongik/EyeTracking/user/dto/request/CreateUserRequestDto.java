package Hongik.EyeTracking.user.dto.request;

import Hongik.EyeTracking.user.domain.User;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class CreateUserRequestDto {
    private String name;
    private String username;
    private String password;
    private String email;

    @Builder
    public static CreateUserRequestDto from(User user) {
        CreateUserRequestDto requestDto = new CreateUserRequestDto();
        requestDto.name = user.getName();
        requestDto.username = user.getUsername();
        requestDto.password = user.getPassword();
        requestDto.email = user.getEmail();

        return requestDto;
    }
}
