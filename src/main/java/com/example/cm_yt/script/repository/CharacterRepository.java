package com.example.cm_yt.script.repository;

import com.example.cm_yt.script.entity.Character;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CharacterRepository extends JpaRepository<Character, Long> {

    /**
     * 특정 스크립트의 모든 캐릭터 조회
     */
    List<Character> findByScriptId(Long scriptId);

    /**
     * 특정 스크립트의 특정 이름 캐릭터 조회
     */
    Optional<Character> findByScriptIdAndName(Long scriptId, String name);
}
