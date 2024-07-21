package Hongik.EyeTracking.post.dto.response;

import Hongik.EyeTracking.post.domain.Post;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ModifyPostResponseDto {
    private Long postId;
    private String content;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime modifiedTime;

    public static ModifyPostResponseDto from(Post post) {
        return new ModifyPostResponseDto(post.getId(), post.getContent(), post.getModifiedDate());
    }
}
