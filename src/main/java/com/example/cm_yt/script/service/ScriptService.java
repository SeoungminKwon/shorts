package com.example.cm_yt.script.service;

import com.example.cm_yt.script.entity.Character;
import com.example.cm_yt.script.entity.Script;
import com.example.cm_yt.script.entity.ScriptLine;
import com.example.cm_yt.script.entity.ScriptStatus;
import com.example.cm_yt.script.repository.CharacterRepository;
import com.example.cm_yt.script.repository.ScriptLineRepository;
import com.example.cm_yt.script.repository.ScriptRepository;
import com.example.cm_yt.shorts.entity.Shorts;
import com.example.cm_yt.shorts.repository.ShortsRepository;
import com.example.cm_yt.user.entity.User;
import com.example.cm_yt.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ScriptService {

    private final ScriptRepository scriptRepository;
    private final ScriptLineRepository scriptLineRepository;
    private final CharacterRepository characterRepository;
    private final UserRepository userRepository;
    private final ShortsRepository shortsRepository;

    /**
     * 스크립트 생성
     */
    @Transactional
    public Script createScript(Long userId, Long shortsId, String title, String originalText) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다: " + userId));

        Shorts shorts = shortsRepository.findById(shortsId)
                .orElseThrow(() -> new IllegalArgumentException("쇼츠를 찾을 수 없습니다: " + shortsId));

        Script script = Script.builder()
                .user(user)
                .shorts(shorts)
                .title(title)
                .originalText(originalText)
                .build();

        return scriptRepository.save(script);
    }

    /**
     * 스크립트 조회
     */
    public Script findById(Long scriptId) {
        return scriptRepository.findById(scriptId)
                .orElseThrow(() -> new IllegalArgumentException("스크립트를 찾을 수 없습니다: " + scriptId));
    }

    /**
     * 사용자의 모든 스크립트 조회
     */
    public Page<Script> findByUserId(Long userId, Pageable pageable) {
        return scriptRepository.findByUserId(userId, pageable);
    }

    /**
     * 사용자의 특정 상태 스크립트 조회
     */
    public Page<Script> findByUserIdAndStatus(Long userId, ScriptStatus status, Pageable pageable) {
        return scriptRepository.findByUserIdAndStatus(userId, status, pageable);
    }

    /**
     * 스크립트 제목 수정
     */
    @Transactional
    public Script updateTitle(Long scriptId, String title) {
        Script script = findById(scriptId);
        script.updateTitle(title);
        return script;
    }

    /**
     * 스크립트 상태 변경
     */
    @Transactional
    public Script updateStatus(Long scriptId, ScriptStatus status) {
        Script script = findById(scriptId);
        script.updateStatus(status);
        return script;
    }

    /**
     * 스크립트 메모 수정
     */
    @Transactional
    public Script updateMemo(Long scriptId, String memo) {
        Script script = findById(scriptId);
        script.updateMemo(memo);
        return script;
    }

    /**
     * 스크립트 라인 추가
     */
    @Transactional
    public ScriptLine addLine(Long scriptId, Integer lineNumber, String text, Long characterId) {
        Script script = findById(scriptId);

        Character character = null;
        if (characterId != null) {
            character = characterRepository.findById(characterId)
                    .orElseThrow(() -> new IllegalArgumentException("캐릭터를 찾을 수 없습니다: " + characterId));
        }

        ScriptLine line = ScriptLine.builder()
                .script(script)
                .lineNumber(lineNumber)
                .text(text)
                .character(character)
                .build();

        script.addLine(line);
        return scriptLineRepository.save(line);
    }

    /**
     * 스크립트 라인 수정
     */
    @Transactional
    public ScriptLine updateLine(Long lineId, String text, Long characterId) {
        ScriptLine line = scriptLineRepository.findById(lineId)
                .orElseThrow(() -> new IllegalArgumentException("스크립트 라인을 찾을 수 없습니다: " + lineId));

        line.updateText(text);

        if (characterId != null) {
            Character character = characterRepository.findById(characterId)
                    .orElseThrow(() -> new IllegalArgumentException("캐릭터를 찾을 수 없습니다: " + characterId));
            line.updateCharacter(character);
        } else {
            line.updateCharacter(null);
        }

        return line;
    }

    /**
     * 스크립트 라인 삭제
     */
    @Transactional
    public void deleteLine(Long lineId) {
        ScriptLine line = scriptLineRepository.findById(lineId)
                .orElseThrow(() -> new IllegalArgumentException("스크립트 라인을 찾을 수 없습니다: " + lineId));

        Script script = line.getScript();
        script.removeLine(line);
        scriptLineRepository.delete(line);
    }

    /**
     * 캐릭터 추가
     */
    @Transactional
    public Character addCharacter(Long scriptId, String name, String description) {
        Script script = findById(scriptId);

        Character character = Character.builder()
                .script(script)
                .name(name)
                .description(description)
                .build();

        script.addCharacter(character);
        return characterRepository.save(character);
    }

    /**
     * 캐릭터 정보 수정
     */
    @Transactional
    public Character updateCharacter(Long characterId, String name, String description) {
        Character character = characterRepository.findById(characterId)
                .orElseThrow(() -> new IllegalArgumentException("캐릭터를 찾을 수 없습니다: " + characterId));

        character.updateCharacter(name, description);
        return character;
    }

    /**
     * 캐릭터 삭제
     */
    @Transactional
    public void deleteCharacter(Long characterId) {
        Character character = characterRepository.findById(characterId)
                .orElseThrow(() -> new IllegalArgumentException("캐릭터를 찾을 수 없습니다: " + characterId));

        Script script = character.getScript();
        script.removeCharacter(character);
        characterRepository.delete(character);
    }

    /**
     * 캐릭터 이미지 생성 결과 저장
     * TODO: 실제 이미지 생성 API 연동 필요
     */
    @Transactional
    public Character updateCharacterImage(Long characterId, String imageUrl, String seedValue) {
        Character character = characterRepository.findById(characterId)
                .orElseThrow(() -> new IllegalArgumentException("캐릭터를 찾을 수 없습니다: " + characterId));

        character.updateImageUrl(imageUrl);
        character.updateSeedValue(seedValue);
        return character;
    }

    /**
     * 스크립트 라인 이미지 생성 결과 저장
     * TODO: 실제 이미지 생성 API 연동 필요
     */
    @Transactional
    public ScriptLine updateLineImage(Long lineId, String imageUrl, String imagePrompt) {
        ScriptLine line = scriptLineRepository.findById(lineId)
                .orElseThrow(() -> new IllegalArgumentException("스크립트 라인을 찾을 수 없습니다: " + lineId));

        line.updateImage(imageUrl, imagePrompt);
        return line;
    }

    /**
     * 스크립트 라인 TTS 생성 결과 저장
     * TODO: 실제 TTS API 연동 필요
     */
    @Transactional
    public ScriptLine updateLineTts(Long lineId, String ttsAudioUrl, Integer audioDurationMs) {
        ScriptLine line = scriptLineRepository.findById(lineId)
                .orElseThrow(() -> new IllegalArgumentException("스크립트 라인을 찾을 수 없습니다: " + lineId));

        line.updateTtsAudio(ttsAudioUrl, audioDurationMs);
        return line;
    }

    /**
     * 스크립트의 모든 라인 조회
     */
    public List<ScriptLine> findLinesByScriptId(Long scriptId) {
        return scriptLineRepository.findByScriptIdOrderByLineNumberAsc(scriptId);
    }

    /**
     * 스크립트의 모든 캐릭터 조회
     */
    public List<Character> findCharactersByScriptId(Long scriptId) {
        return characterRepository.findByScriptId(scriptId);
    }

    /**
     * 이미지가 생성되지 않은 라인 조회
     */
    public List<ScriptLine> findLinesWithoutImage(Long scriptId) {
        return scriptLineRepository.findLinesWithoutImage(scriptId);
    }

    /**
     * TTS가 생성되지 않은 라인 조회
     */
    public List<ScriptLine> findLinesWithoutTts(Long scriptId) {
        return scriptLineRepository.findLinesWithoutTts(scriptId);
    }
}
