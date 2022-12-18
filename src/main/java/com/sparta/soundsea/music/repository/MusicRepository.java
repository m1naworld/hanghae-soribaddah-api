package com.sparta.soundsea.music.repository;

import com.sparta.soundsea.music.entity.Music;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MusicRepository extends JpaRepository<Music, Long>{

}
