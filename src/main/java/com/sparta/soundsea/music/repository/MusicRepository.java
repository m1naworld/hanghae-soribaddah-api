package com.sparta.soundsea.music.repository;

import com.sparta.soundsea.music.entity.Music;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MusicRepository extends JpaRepository<Music, Long>{

    List<Music> findAllByOrderByLastModifiedAtDesc();
}
