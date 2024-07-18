package Hongik.EyeTracking.board.dto.response;

import Hongik.EyeTracking.board.domain.Board;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class CreateBoardResponseDto {
    private String name;
    private Long boardId;

    public static CreateBoardResponseDto from(Board board) {
        return new CreateBoardResponseDto(board.getName(), board.getId());
    }
}
