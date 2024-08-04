package Hongik.EyeTracking.comment.dto.response;

import Hongik.EyeTracking.comment.domain.Comment;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ReadCommentResponseDto {
    private Long commentId;
    private String content;
    private String commenterName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime createdTime;

    List<ReadReplyResponseDto> replies = new ArrayList<>();

    public static ReadCommentResponseDto of(Comment comment, List<Comment> replies) {
        ReadCommentResponseDto response = ReadCommentResponseDto.builder()
                .commentId(comment.getId())
                .content(comment.getContent())
                .commenterName(comment.getCommenter().getName())
                .createdTime(comment.getCreatedDate())
                .replies(new ArrayList<>())
                .build();

        replies.forEach(reply ->
                response.replies.add(ReadReplyResponseDto.from(reply))
        );

        return response;
    }
}
