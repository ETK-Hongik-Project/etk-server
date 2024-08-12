package Hongik.EyeTracking.comment.dto.response;

import Hongik.EyeTracking.comment.domain.Comment;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ReadReplyResponseDto {
    private Long commentId;
    private String content;
    private String commenterName;
    private Boolean isDeleted;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yy-MM-dd HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime createdTime;

    public static ReadReplyResponseDto from(Comment reply) {

        return ReadReplyResponseDto.builder()
                .commentId(reply.getId())
                .content(reply.getContent())
                .commenterName(reply.getCommenter().getName())
                .createdTime(reply.getCreatedDate())
                .isDeleted(reply.isDeleted())
                .build();
    }
}
