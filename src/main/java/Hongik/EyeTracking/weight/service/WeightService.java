package Hongik.EyeTracking.weight.service;

import Hongik.EyeTracking.common.response.error.ErrorCode;
import Hongik.EyeTracking.common.response.error.exception.DuplicateException;
import Hongik.EyeTracking.common.response.error.exception.NotFoundException;
import Hongik.EyeTracking.common.response.error.exception.ResourceAlreadyUpdatedException;
import Hongik.EyeTracking.image.domain.Image;
import Hongik.EyeTracking.image.dto.ImageResponseDto;
import Hongik.EyeTracking.user.domain.User;
import Hongik.EyeTracking.user.repository.UserRepository;
import Hongik.EyeTracking.weight.domain.Weight;
import Hongik.EyeTracking.weight.dto.WeightResponseDto;
import Hongik.EyeTracking.weight.repository.WeightRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class WeightService {
    private final WeightRepository weightRepository;
    private final UserRepository userRepository;

    // 폴더 경로
    @Value("${upload.directory.weight}")
    private String uploadDir;

    @Transactional
    public WeightResponseDto createWeight(String username, MultipartFile file) throws IOException {
        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new NotFoundException(ErrorCode.USER_NOT_FOUND)
        );
        // 한 user당 가중치는 하나밖에 가질 수 없음.
        weightRepository.findByUserId(user.getId())
                .ifPresent(weight -> new DuplicateException(ErrorCode.WEIGHT_ALREADY_EXISTS));

        if (file.isEmpty()) {
            throw new IOException("Failed to store empty file.");
        }

        // 파일 저장 경로
        String fileDir = uploadDir + '/' + user.getId() + '/' + file.getOriginalFilename();

        // 파일 저장
        Path path = Paths.get(fileDir);
        Files.createDirectories(path.getParent());
        Files.write(path, file.getBytes());

        // DB에 가중치 정보 저장
        Weight weight = Weight.builder()
                .fileName(file.getOriginalFilename())
                .filePath(path.toString())
                .user(user)
                .build();

        return WeightResponseDto.from(weightRepository.save(weight));
    }

    @Transactional
    public WeightResponseDto createWeight(Long userId, MultipartFile file) throws IOException {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException(ErrorCode.USER_NOT_FOUND)
        );
        // 한 user당 가중치는 하나밖에 가질 수 없음.
        weightRepository.findByUserId(user.getId())
                .ifPresent(weight -> new DuplicateException(ErrorCode.WEIGHT_ALREADY_EXISTS));

        if (file.isEmpty()) {
            throw new IOException("Failed to store empty file.");
        }

        // 파일 저장 경로
        String fileDir = uploadDir + '/' + user.getId() + '/' + file.getOriginalFilename();

        // 파일 저장
        Path path = Paths.get(fileDir);
        Files.createDirectories(path.getParent());
        Files.write(path, file.getBytes());

        // DB에 가중치 정보 저장
        Weight weight = Weight.builder()
                .fileName(file.getOriginalFilename())
                .filePath(path.toString())
                .user(user)
                .build();

        return WeightResponseDto.from(weightRepository.save(weight));
    }

    @Transactional
    public byte[] getWeight(String username) throws IOException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));

        Weight weight = weightRepository.findByUserId(user.getId()).orElseThrow(() ->
                new NotFoundException(ErrorCode.WEIGHT_NOT_FOUND));

        // 이미 가중치가 업데이트 된 경우 업데이트 불가
        if (weight.getIsUpdated()) {
            throw new ResourceAlreadyUpdatedException("이미 업데이트 된 가중치입니다.");
        }

        FileInputStream weightInputStream = new FileInputStream(weight.getFilePath());

        byte[] weightByteArray = IOUtils.toByteArray(weightInputStream);
        weightInputStream.close();

        // 업데이트 여부 true 설정
        weight.updateIsUpdated(true);

        return weightByteArray;
    }

    @Transactional
    public void deleteWeight(String username) throws IOException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));

        Weight weight = weightRepository.findByUserId(user.getId()).orElseThrow(() ->
                new NotFoundException(ErrorCode.WEIGHT_NOT_FOUND));

        try {
            Path path = Paths.get(weight.getFilePath());
            Files.deleteIfExists(path);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        weightRepository.delete(weight);
    }

    @Transactional
    public void deleteWeightIfExists(String username) throws IOException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));

        weightRepository.findByUserId(user.getId())
                .ifPresent(weight -> {
                    try {
                        Path path = Paths.get(weight.getFilePath());
                        Files.deleteIfExists(path);
                    } catch (IOException e) {
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }
                    weightRepository.delete(weight);
                });
    }

    @Transactional
    public void deleteWeightIfExists(Long userId) throws IOException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));

        weightRepository.findByUserId(user.getId())
                .ifPresent(weight -> {
                    try {
                        Path path = Paths.get(weight.getFilePath());
                        Files.deleteIfExists(path);
                    } catch (IOException e) {
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }
                    weightRepository.delete(weight);
                });
    }
}
