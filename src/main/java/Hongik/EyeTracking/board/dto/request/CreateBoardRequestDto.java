package Hongik.EyeTracking.board.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CreateBoardRequestDto {
    @NotBlank(message = "게시판 이름이 입력되지 않았습니다")
    private String name;
}
