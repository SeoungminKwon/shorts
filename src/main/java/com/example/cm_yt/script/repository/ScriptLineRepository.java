package com.example.cm_yt.script.repository;

import com.example.cm_yt.script.entity.ScriptLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScriptLineRepository extends JpaRepository<ScriptLine, Long> {

    /**
     * 특정 스크립트의 모든 라인 조회 (순서대로)
     */
    List<ScriptLine> findByScriptIdOrderByLineNumberAsc(Long scriptId);

    /**
     * 특정 스크립트의 특정 라인 번호 조회
     */
    @Query("SELECT sl FROM ScriptLine sl WHERE sl.script.id = :scriptId AND sl.lineNumber = :lineNumber")
    ScriptLine findByScriptIdAndLineNumber(@Param("scriptId") Long scriptId, @Param("lineNumber") Integer lineNumber);

    /**
     * 이미지가 생성되지 않은 라인 조회
     */
    @Query("SELECT sl FROM ScriptLine sl WHERE sl.script.id = :scriptId AND sl.imageUrl IS NULL ORDER BY sl.lineNumber ASC")
    List<ScriptLine> findLinesWithoutImage(@Param("scriptId") Long scriptId);

    /**
     * TTS가 생성되지 않은 라인 조회
     */
    @Query("SELECT sl FROM ScriptLine sl WHERE sl.script.id = :scriptId AND sl.ttsAudioUrl IS NULL ORDER BY sl.lineNumber ASC")
    List<ScriptLine> findLinesWithoutTts(@Param("scriptId") Long scriptId);
}
