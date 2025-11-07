package com.example.cm_yt.script.controller;

import com.example.cm_yt.global.ApiResponse;
import com.example.cm_yt.script.dto.*;
import com.example.cm_yt.script.entity.Character;
import com.example.cm_yt.script.entity.Script;
import com.example.cm_yt.script.entity.ScriptLine;
import com.example.cm_yt.script.entity.ScriptStatus;
import com.example.cm_yt.script.service.ScriptService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Script", description = "스크립트 관리 API")
@RestController
@RequestMapping("/api/scripts")
@RequiredArgsConstructor
public class ScriptController {

    private final ScriptService scriptService;

    @Operation(summary = "스크립트 생성", description = "쇼츠로부터 새로운 스크립트를 생성합니다")
    @PostMapping
    public ApiResponse<ScriptDetailResponse> createScript(
            @RequestParam Long userId,
            @Valid @RequestBody ScriptCreateRequest request) {
        Script script = scriptService.createScript(
                userId,
                request.getShortsId(),
                request.getTitle(),
                request.getOriginalText()
        );
        return ApiResponse.success(ScriptDetailResponse.from(script));
    }

    @Operation(summary = "스크립트 상세 조회", description = "스크립트 ID로 상세 정보를 조회합니다")
    @GetMapping("/{scriptId}")
    public ApiResponse<ScriptDetailResponse> getScript(@PathVariable Long scriptId) {
        Script script = scriptService.findById(scriptId);
        return ApiResponse.success(ScriptDetailResponse.from(script));
    }

    @Operation(summary = "사용자 스크립트 목록", description = "사용자의 모든 스크립트를 조회합니다")
    @GetMapping("/user/{userId}")
    public ApiResponse<Page<ScriptResponse>> getUserScripts(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Script> scripts = scriptService.findByUserId(userId, pageable);
        Page<ScriptResponse> responses = scripts.map(ScriptResponse::from);
        return ApiResponse.success(responses);
    }

    @Operation(summary = "상태별 스크립트 조회", description = "사용자의 특정 상태 스크립트를 조회합니다")
    @GetMapping("/user/{userId}/status/{status}")
    public ApiResponse<Page<ScriptResponse>> getUserScriptsByStatus(
            @PathVariable Long userId,
            @PathVariable ScriptStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Script> scripts = scriptService.findByUserIdAndStatus(userId, status, pageable);
        Page<ScriptResponse> responses = scripts.map(ScriptResponse::from);
        return ApiResponse.success(responses);
    }

    @Operation(summary = "스크립트 제목 수정", description = "스크립트 제목을 수정합니다")
    @PutMapping("/{scriptId}/title")
    public ApiResponse<ScriptDetailResponse> updateTitle(
            @PathVariable Long scriptId,
            @RequestParam String title) {
        Script script = scriptService.updateTitle(scriptId, title);
        return ApiResponse.success(ScriptDetailResponse.from(script));
    }

    @Operation(summary = "스크립트 상태 변경", description = "스크립트 상태를 변경합니다")
    @PutMapping("/{scriptId}/status")
    public ApiResponse<ScriptDetailResponse> updateStatus(
            @PathVariable Long scriptId,
            @RequestParam ScriptStatus status) {
        Script script = scriptService.updateStatus(scriptId, status);
        return ApiResponse.success(ScriptDetailResponse.from(script));
    }

    @Operation(summary = "스크립트 메모 수정", description = "스크립트 메모를 수정합니다")
    @PutMapping("/{scriptId}/memo")
    public ApiResponse<ScriptDetailResponse> updateMemo(
            @PathVariable Long scriptId,
            @RequestParam String memo) {
        Script script = scriptService.updateMemo(scriptId, memo);
        return ApiResponse.success(ScriptDetailResponse.from(script));
    }

    @Operation(summary = "스크립트 라인 추가", description = "스크립트에 새로운 라인을 추가합니다")
    @PostMapping("/{scriptId}/lines")
    public ApiResponse<ScriptLineResponse> addLine(
            @PathVariable Long scriptId,
            @Valid @RequestBody ScriptLineRequest request) {
        ScriptLine line = scriptService.addLine(
                scriptId,
                request.getLineNumber(),
                request.getText(),
                request.getCharacterId()
        );
        return ApiResponse.success(ScriptLineResponse.from(line));
    }

    @Operation(summary = "스크립트 라인 수정", description = "스크립트 라인을 수정합니다")
    @PutMapping("/lines/{lineId}")
    public ApiResponse<ScriptLineResponse> updateLine(
            @PathVariable Long lineId,
            @Valid @RequestBody ScriptLineRequest request) {
        ScriptLine line = scriptService.updateLine(
                lineId,
                request.getText(),
                request.getCharacterId()
        );
        return ApiResponse.success(ScriptLineResponse.from(line));
    }

    @Operation(summary = "스크립트 라인 삭제", description = "스크립트 라인을 삭제합니다")
    @DeleteMapping("/lines/{lineId}")
    public ApiResponse<String> deleteLine(@PathVariable Long lineId) {
        scriptService.deleteLine(lineId);
        return ApiResponse.success("라인이 삭제되었습니다");
    }

    @Operation(summary = "캐릭터 추가", description = "스크립트에 새로운 캐릭터를 추가합니다")
    @PostMapping("/{scriptId}/characters")
    public ApiResponse<CharacterResponse> addCharacter(
            @PathVariable Long scriptId,
            @Valid @RequestBody CharacterRequest request) {
        Character character = scriptService.addCharacter(
                scriptId,
                request.getName(),
                request.getDescription()
        );
        return ApiResponse.success(CharacterResponse.from(character));
    }

    @Operation(summary = "캐릭터 수정", description = "캐릭터 정보를 수정합니다")
    @PutMapping("/characters/{characterId}")
    public ApiResponse<CharacterResponse> updateCharacter(
            @PathVariable Long characterId,
            @Valid @RequestBody CharacterRequest request) {
        Character character = scriptService.updateCharacter(
                characterId,
                request.getName(),
                request.getDescription()
        );
        return ApiResponse.success(CharacterResponse.from(character));
    }

    @Operation(summary = "캐릭터 삭제", description = "캐릭터를 삭제합니다")
    @DeleteMapping("/characters/{characterId}")
    public ApiResponse<String> deleteCharacter(@PathVariable Long characterId) {
        scriptService.deleteCharacter(characterId);
        return ApiResponse.success("캐릭터가 삭제되었습니다");
    }

    @Operation(summary = "캐릭터 이미지 생성", description = "캐릭터 이미지를 생성하고 저장합니다 (TODO: API 연동)")
    @PostMapping("/characters/{characterId}/generate-image")
    public ApiResponse<CharacterResponse> generateCharacterImage(
            @PathVariable Long characterId,
            @Valid @RequestBody ImageGenerationRequest request) {
        // TODO: 실제 이미지 생성 API 호출
        String mockImageUrl = "https://example.com/images/" + characterId + ".png";
        String mockSeedValue = "seed_" + System.currentTimeMillis();

        Character character = scriptService.updateCharacterImage(characterId, mockImageUrl, mockSeedValue);
        return ApiResponse.success(CharacterResponse.from(character));
    }

    @Operation(summary = "라인 이미지 생성", description = "스크립트 라인의 이미지를 생성합니다 (TODO: API 연동)")
    @PostMapping("/lines/{lineId}/generate-image")
    public ApiResponse<ScriptLineResponse> generateLineImage(
            @PathVariable Long lineId,
            @Valid @RequestBody ImageGenerationRequest request) {
        // TODO: 실제 이미지 생성 API 호출
        String mockImageUrl = "https://example.com/images/line_" + lineId + ".png";

        ScriptLine line = scriptService.updateLineImage(lineId, mockImageUrl, request.getPrompt());
        return ApiResponse.success(ScriptLineResponse.from(line));
    }

    @Operation(summary = "라인 TTS 생성", description = "스크립트 라인의 TTS 음성을 생성합니다 (TODO: API 연동)")
    @PostMapping("/lines/{lineId}/generate-tts")
    public ApiResponse<ScriptLineResponse> generateLineTts(
            @PathVariable Long lineId,
            @Valid @RequestBody TtsGenerationRequest request) {
        // TODO: 실제 TTS API 호출
        String mockTtsUrl = "https://example.com/audio/line_" + lineId + ".mp3";
        Integer mockDuration = 3000; // 3초

        ScriptLine line = scriptService.updateLineTts(lineId, mockTtsUrl, mockDuration);
        return ApiResponse.success(ScriptLineResponse.from(line));
    }

    @Operation(summary = "이미지 미생성 라인 조회", description = "아직 이미지가 생성되지 않은 라인들을 조회합니다")
    @GetMapping("/{scriptId}/lines/without-image")
    public ApiResponse<List<ScriptLineResponse>> getLinesWithoutImage(@PathVariable Long scriptId) {
        List<ScriptLine> lines = scriptService.findLinesWithoutImage(scriptId);
        List<ScriptLineResponse> responses = lines.stream()
                .map(ScriptLineResponse::from)
                .collect(Collectors.toList());
        return ApiResponse.success(responses);
    }

    @Operation(summary = "TTS 미생성 라인 조회", description = "아직 TTS가 생성되지 않은 라인들을 조회합니다")
    @GetMapping("/{scriptId}/lines/without-tts")
    public ApiResponse<List<ScriptLineResponse>> getLinesWithoutTts(@PathVariable Long scriptId) {
        List<ScriptLine> lines = scriptService.findLinesWithoutTts(scriptId);
        List<ScriptLineResponse> responses = lines.stream()
                .map(ScriptLineResponse::from)
                .collect(Collectors.toList());
        return ApiResponse.success(responses);
    }
}
