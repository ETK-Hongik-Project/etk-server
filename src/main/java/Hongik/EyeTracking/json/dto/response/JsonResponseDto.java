package Hongik.EyeTracking.json.dto.response;

import Hongik.EyeTracking.json.domain.Json;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class JsonResponseDto {
    private Long jsonId;
    private String fileName;
    private String filePath;

    public static JsonResponseDto from(Json json) {
        JsonResponseDto jsonResponseDto = JsonResponseDto.builder()
                .jsonId(json.getId())
                .fileName(json.getFileName())
                .filePath(json.getFilePath())
                .build();

        return jsonResponseDto;
    }
}
