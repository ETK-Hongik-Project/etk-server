package Hongik.EyeTracking;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.io.File;
import java.util.TimeZone;

@SpringBootApplication
@EnableJpaAuditing
public class EyeTrackingApplication {

    @Value("${upload.directory.image}")
    private String imageUploadDirectory;

    @Value("${upload.directory.weight}")
    private String weightUploadDirectory;

    @Value("${upload.directory.json}")
    private String jsonUploadDirectory;

    @PostConstruct
    public void init() {
        File imageDirectory = new File(imageUploadDirectory);
        if (!imageDirectory.exists()) {
            imageDirectory.mkdirs(); // 이미지 디렉터리 생성
        }

        File weightDirectory = new File(weightUploadDirectory);
        if (!weightDirectory.exists()) {
            weightDirectory.mkdirs(); // 가중치 디렉터리 생성
        }

        File jsonDirectory = new File(jsonUploadDirectory);
        if (!jsonDirectory.exists()) {
            jsonDirectory.mkdirs(); // json 디렉터리 생성
        }
    }

    @PostConstruct
    public void setTimeZone() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
    }

    public static void main(String[] args) {
        SpringApplication.run(EyeTrackingApplication.class, args);
    }
}
