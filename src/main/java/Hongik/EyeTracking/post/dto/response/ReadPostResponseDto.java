package Hongik.EyeTracking.post.dto.response;

import Hongik.EyeTracking.post.domain.Post;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ReadPostResponseDto {
    private Long postId;

    private String title;
    private String content;
    private String authorName;

    public static ReadPostResponseDto from(Post post) {
        ReadPostResponseDto response = new ReadPostResponseDto();

        response.title = post.getTitle();
        response.content = post.getContent();
        response.authorName = post.isAnonymous() || post.getAuthor() == null ? "익명" : post.getAuthor().getUsername();

        return response;
    }

    public static ReadPostResponseDto of(Post post, String userName) {
        ReadPostResponseDto response = new ReadPostResponseDto();

        response.title = post.getTitle();
        response.content = post.getContent();
        response.authorName = post.isAnonymous()  ? "익명" : userName;

        return response;
    }
}
