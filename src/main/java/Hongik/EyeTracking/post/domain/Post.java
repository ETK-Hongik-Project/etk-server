package Hongik.EyeTracking.post.domain;

import Hongik.EyeTracking.board.domain.Board;
import Hongik.EyeTracking.common.inheritance.BaseEntity;
import Hongik.EyeTracking.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = PROTECTED)
public class Post extends BaseEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @Column(updatable = false, nullable = false)
    private String title;
    @Column(length = 1000)
    private String content;

    private boolean isAnonymous;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id") // join 한 user의 fk 명을 "user_id"로 지정. 연관관계 매핑에는 @JoinColumn은 영향을 주지 않음.
    private User author;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @Builder
    public Post(String title, String content, boolean isAnonymous, Board board, User author) {
        this.title = title;
        this.content = content;
        this.isAnonymous = isAnonymous;
        this.board = board;
        this.author = author;
    }

    public void updateAuthor(User user) {
        this.author = user;
    }

    public void updateContent(String content) {
        this.content = content;
    }
}
