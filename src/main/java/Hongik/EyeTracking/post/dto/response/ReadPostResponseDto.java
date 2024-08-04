package Hongik.EyeTracking.post.dto.response;

import Hongik.EyeTracking.post.domain.Post;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ReadPostResponseDto {
    private Long postId;

    private String title;
    private String content;
    private String authorName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime createdTime;

    public static ReadPostResponseDto from(Post post) {
        ReadPostResponseDto response = new ReadPostResponseDto();

        response.postId = post.getId();
        response.title = post.getTitle();
        response.content = post.getContent();
        response.authorName = post.isAnonymous() || post.getAuthor() == null ? "익명" : post.getAuthor().getName();
        response.createdTime = post.getCreatedDate();

        return response;
    }

    public static ReadPostResponseDto of(Post post, String authorName) {
        ReadPostResponseDto response = new ReadPostResponseDto();

        response.postId = post.getId();
        response.title = post.getTitle();
        response.content = post.getContent();
        response.authorName = post.isAnonymous()  ? "익명" : authorName;
        response.createdTime = post.getCreatedDate();

        return response;
    }
}
