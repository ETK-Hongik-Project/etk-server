package Hongik.EyeTracking.json.domain;

import Hongik.EyeTracking.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Json {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "json_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String fileName;

    @Column(nullable = false, unique = true)
    private String filePath;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public Json(String fileName, String filePath, User user) {
        this.fileName = fileName;
        this.filePath = filePath;
        this.user = user;
    }
}
