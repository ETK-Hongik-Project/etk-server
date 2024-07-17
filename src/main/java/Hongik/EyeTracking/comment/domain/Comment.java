package Hongik.EyeTracking.comment.domain;

import Hongik.EyeTracking.common.inheritance.BaseEntity;
import Hongik.EyeTracking.post.domain.Post;
import Hongik.EyeTracking.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PROTECTED)
@ToString(exclude = {"commenter", "post", "parentComment"})
public class Comment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @Column(nullable = false, length = 1000)
    private String content;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User commenter;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "parent_comment_id")
    private Comment parentComment;

    @Builder
    public Comment(String content, User commenter, Post post, Comment parentComment) {
        this.content = content;
        this.commenter = commenter;
        this.post = post;
        this.parentComment = parentComment;
    }

    // 연관관계 제거
    public void updateCommenter(User user) {
        this.commenter = user;
    }
}
