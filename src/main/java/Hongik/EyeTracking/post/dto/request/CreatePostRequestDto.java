package Hongik.EyeTracking.post.dto.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreatePostRequestDto {
    @NotBlank(message = "title이 입력되지 않음")
    private String title;
    @NotBlank(message = "content가 입력되지 않음")
    private String content;
    @NotNull(message = "익명 여부가 입력되지 않음")
    private Boolean isAnonymous;
}
