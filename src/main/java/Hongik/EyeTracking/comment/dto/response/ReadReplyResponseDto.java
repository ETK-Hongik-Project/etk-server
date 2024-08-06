package Hongik.EyeTracking.comment.dto.response;

import Hongik.EyeTracking.comment.domain.Comment;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ReadReplyResponseDto {
    private Long commentId;
    private String content;
    private String commenterName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yy-MM-dd HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime createdTime;

    public static ReadReplyResponseDto from(Comment reply) {
        return new ReadReplyResponseDto(reply.getId(), reply.getContent(), reply.getCommenter().getName(), reply.getCreatedDate());
    }
}
