package Hongik.EyeTracking.json.controller;

import Hongik.EyeTracking.auth.interfaces.CurrentUserUsername;
import Hongik.EyeTracking.json.dto.request.JsonUploadRequestDto;
import Hongik.EyeTracking.json.dto.response.JsonResponseDto;
import Hongik.EyeTracking.json.service.JsonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Slf4j
public class JsonController {
    private final JsonService jsonService;

    @PostMapping("/json")
    public ResponseEntity<JsonResponseDto> uploadJson(@CurrentUserUsername String username, @RequestBody JsonUploadRequestDto requestDto) {
        String json = requestDto.getData();
        JsonResponseDto response = jsonService.createJson(username, json);
        
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/json/{jsonId}")
    public JsonResponseDto readJson(@CurrentUserUsername String username, @PathVariable Long jsonId) {
        return jsonService.getJson(username, jsonId);
    }

    @GetMapping("/json")
    public List<JsonResponseDto> readJsons(@CurrentUserUsername String username) {
        return jsonService.getJsons(username);
    }

    @DeleteMapping("/json/{jsonId}")
    public void deleteJson(@CurrentUserUsername String username, @PathVariable Long jsonId) {
        jsonService.deleteJson(username, jsonId);
    }

    @DeleteMapping("/json")
    public void deleteJsons(@CurrentUserUsername String username) {
        jsonService.deleteAllJsons(username);
    }
}
