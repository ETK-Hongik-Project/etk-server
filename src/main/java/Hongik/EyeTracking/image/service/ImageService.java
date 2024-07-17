package Hongik.EyeTracking.image.service;

import Hongik.EyeTracking.common.response.error.ErrorCode;
import Hongik.EyeTracking.common.response.error.exception.NotFoundException;
import Hongik.EyeTracking.image.domain.Image;
import Hongik.EyeTracking.image.dto.ImageResponseDto;
import Hongik.EyeTracking.image.repository.ImageRepository;
import Hongik.EyeTracking.user.domain.User;
import Hongik.EyeTracking.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import org.apache.commons.io.IOUtils;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ImageService {
    private final ImageRepository imageRepository;
    private final UserRepository userRepository;

    // 폴더 경로
    @Value("${upload.directory}")
    private String uploadDir;

    @Transactional
    public ImageResponseDto createImage(String username, MultipartFile file) throws IOException {
        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new NotFoundException(ErrorCode.USER_NOT_FOUND)
        );

        if (file.isEmpty()) {
            throw new IOException("Failed to store empty file.");
        }

        // 파일 저장 경로
        String fileDir = uploadDir + '/' + file.getOriginalFilename();

        // 파일 저장
        Path path = Paths.get(fileDir);
        Files.createDirectories(path.getParent());
        Files.write(path, file.getBytes());

        // DB에 이미지 정보 저장
        Image image = Image.builder()
                .fileName(file.getOriginalFilename())
                .filePath(path.toString())
                .user(user)
                .build();

        return ImageResponseDto.from(imageRepository.save(image));
    }

    public List<ImageResponseDto> getImages(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new NotFoundException(ErrorCode.USER_NOT_FOUND)
        );

        List<ImageResponseDto> response = new ArrayList<>();
        imageRepository.findByUserId(user.getId()).forEach(image -> {
            response.add(ImageResponseDto.from(image));
        });

        return response;
    }

    public byte[] getImage(String username, Long imageId) throws IOException {
        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new NotFoundException(ErrorCode.USER_NOT_FOUND)
        );

        Image image = imageRepository.findById(imageId).orElseThrow(() ->
                new NotFoundException(ErrorCode.NOT_FOUND)
        );

        InputStream imageStream = new FileInputStream(image.getFilePath());

        byte[] imageByteArray = IOUtils.toByteArray(imageStream);
		imageStream.close();

        return imageByteArray;
    }

    @Transactional
    public void deleteImages(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new NotFoundException(ErrorCode.USER_NOT_FOUND)
        );

        List<Image> images = imageRepository.findByUserId(user.getId());

        System.out.println("images.size() = " + images.size());

        // 파일 시스템에서 이미지 제거
        for (Image image : images) {
            try {
                System.out.println("image.getFilePath() = " + image.getFilePath());

                Path path = Paths.get(image.getFilePath());
                Files.deleteIfExists(path);

                imageRepository.delete(image);
            } catch (IOException e) {
                // 로그를 출력하거나, 예외를 던질 수 있습니다.
                e.printStackTrace();
            }
        }
    }


}
