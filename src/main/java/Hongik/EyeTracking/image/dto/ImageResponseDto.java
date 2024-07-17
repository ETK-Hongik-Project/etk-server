package Hongik.EyeTracking.image.dto;

import Hongik.EyeTracking.image.domain.Image;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ImageResponseDto {
    private Long id;
    private String name;
    private String path;

    @Builder
    public static ImageResponseDto from(Image image) {
        ImageResponseDto responseDto = new ImageResponseDto();

        responseDto.id = image.getId();
        responseDto.name = image.getFileName();
        responseDto.path = image.getFilePath();

        return responseDto;
    }
}
