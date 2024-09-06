package Hongik.EyeTracking.weight.dto;

import Hongik.EyeTracking.image.domain.Image;
import Hongik.EyeTracking.image.dto.ImageResponseDto;
import Hongik.EyeTracking.weight.domain.Weight;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WeightResponseDto {
    private Long id;
    private String name;
    private String path;

    @Builder
    public static WeightResponseDto from(Weight weight) {
        WeightResponseDto responseDto = new WeightResponseDto();

        responseDto.id = weight.getId();
        responseDto.name = weight.getFileName();
        responseDto.path = weight.getFilePath();

        return responseDto;
    }
}
