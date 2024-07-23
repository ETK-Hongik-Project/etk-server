package Hongik.EyeTracking.user.dto.request;

import Hongik.EyeTracking.user.domain.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class CreateUserRequestDto {
    @NotBlank(message = "이름이 입력되지 않았습니다")
    private String name;
    @NotBlank(message = "아이디가 입력되지 않았습니다")
    private String username;
    @NotBlank(message = "비밀번호가 입력되지 않았습니다")
    private String password;
    @Pattern(regexp="^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])+[.][a-zA-Z]{2,3}$", message="올바른 형식이 아닙니다")
    @NotBlank(message = "이메일이 입력되지 않았습니다")
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
