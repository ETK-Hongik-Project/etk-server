package Hongik.EyeTracking.image.domain;

import Hongik.EyeTracking.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String fileName;

    @Column(nullable = false, unique = true)
    private String filePath;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public Image(String fileName, String filePath, User user) {
        this.fileName = fileName;
        this.filePath = filePath;
        this.user = user;
    }
}
