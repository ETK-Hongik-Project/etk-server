package Hongik.EyeTracking.comment.dto.response;

import Hongik.EyeTracking.comment.domain.Comment;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ReadUserCommentResponseDto {
    private String postTitle;
    private String postContent;

    private String content;
    private String commenterName;

    public static ReadUserCommentResponseDto from(Comment comment) {
        return new ReadUserCommentResponseDto(
                comment.getPost().getTitle(),
                comment.getPost().getContent(),
                comment.getContent(),
                comment.getCommenter().getName());
    }
}
