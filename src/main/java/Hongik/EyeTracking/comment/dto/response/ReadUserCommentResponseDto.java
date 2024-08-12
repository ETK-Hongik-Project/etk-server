package Hongik.EyeTracking.comment.dto.response;

import Hongik.EyeTracking.comment.domain.Comment;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ReadUserCommentResponseDto {
    private String postTitle;
    private String postContent;

    private String content;
    private String commenterName;
    private Boolean isDeleted;

    public static ReadUserCommentResponseDto from(Comment comment) {
        return ReadUserCommentResponseDto.builder()
                .postTitle(comment.getPost().getTitle())
                .postContent(comment.getPost().getContent())
                .content(comment.getContent())
                .commenterName(comment.getCommenter().getName())
                .isDeleted(comment.isDeleted())
                .build();
    }
}
