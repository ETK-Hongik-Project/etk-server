package Hongik.EyeTracking.json.service;

import Hongik.EyeTracking.common.response.error.ErrorCode;
import Hongik.EyeTracking.common.response.error.exception.BadRequestException;
import Hongik.EyeTracking.common.response.error.exception.NotFoundException;
import Hongik.EyeTracking.json.domain.Json;
import Hongik.EyeTracking.json.dto.response.JsonResponseDto;
import Hongik.EyeTracking.json.repository.JsonRepository;
import Hongik.EyeTracking.user.domain.User;
import Hongik.EyeTracking.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class JsonService {
    @Value("${upload.directory.json}")
    private String uploadDir;

    private final JsonRepository jsonRepository;
    private final UserRepository userRepository;

    @Transactional
    public JsonResponseDto createJson(String username, String jsonString) {
        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new NotFoundException(ErrorCode.USER_NOT_FOUND)
        );

        long cur = new Date().getTime();

        String fileName = "images_" + cur + ".json";
        String fileDir = uploadDir + '/' + user.getId();

        // JSON 파일을 저장할 디렉토리 생성
        File directory = new File(fileDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // JSON 파일 작성
        try (FileWriter fileWriter = new FileWriter(fileDir + '/' + fileName)) {
            fileWriter.write(jsonString);
            fileWriter.flush();
        } catch (IOException e) {
            throw new RuntimeException("파일 저장 중 오류가 발생했습니다.", e);
        }

        Json json = Json.builder()
                .user(user)
                .fileName(fileName)
                .filePath(fileDir+'/'+fileName)
                .build();

        jsonRepository.save(json);

        return JsonResponseDto.from(json);
    }

    public List<JsonResponseDto> getJsons(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new NotFoundException(ErrorCode.USER_NOT_FOUND)
        );
        List<JsonResponseDto> response = new ArrayList();
        jsonRepository.findByUserId(user.getId())
                .forEach(json -> response.add(JsonResponseDto.from(json)));

        return response;
    }

    public JsonResponseDto getJson(String username, Long jsonId) {
        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new NotFoundException(ErrorCode.USER_NOT_FOUND)
        );

        Json json = jsonRepository.findByUserIdAndId(user.getId(), jsonId).orElseThrow(
                () -> new BadRequestException(ErrorCode.NOT_USER_JSON)
        );

        return JsonResponseDto.from(json);
    }

    @Transactional
    public void deleteJson(String username, Long jsonId) {
        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new NotFoundException(ErrorCode.USER_NOT_FOUND)
        );
        Json json = jsonRepository.findByUserIdAndId(user.getId(), jsonId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_USER_JSON));
        try {
            Path path = Paths.get(json.getFilePath());
            Files.deleteIfExists(path);
            jsonRepository.deleteById(jsonId);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Transactional
    public void deleteAllJsons(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new NotFoundException(ErrorCode.USER_NOT_FOUND)
        );

        List<Json> jsons = jsonRepository.findByUserId(user.getId());

        for (Json json : jsons) {
            try {
                System.out.println("image.getFilePath() = " + json.getFilePath());

                Path path = Paths.get(json.getFilePath());
                Files.deleteIfExists(path);

                jsonRepository.delete(json);
            } catch (IOException e) {
                // 로그를 출력하거나, 예외를 던질 수 있습니다.
                e.printStackTrace();
            }
        }
    }
}
