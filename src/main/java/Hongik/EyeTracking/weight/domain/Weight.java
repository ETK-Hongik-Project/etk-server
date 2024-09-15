package Hongik.EyeTracking.weight.domain;

import Hongik.EyeTracking.common.inheritance.BaseEntity;
import Hongik.EyeTracking.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Weight extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "weight_id")
    private Long id;

    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false, unique = true)
    private String filePath;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private Boolean isUpdated;

    @Builder
    public Weight(String fileName, String filePath, User user) {
        this.fileName = fileName;
        this.filePath = filePath;
        this.user = user;
        this.isUpdated = false;
    }

    public void updateIsUpdated(Boolean isUpdated) {
        this.isUpdated = isUpdated;
    }
}
