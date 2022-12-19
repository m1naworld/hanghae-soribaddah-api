package com.sparta.soundsea.comment.entity;

import com.sparta.soundsea.common.entity.Timestamped;
import com.sparta.soundsea.music.entity.Music;
import com.sparta.soundsea.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
public class Comment extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String contents;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable=false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="MUSIC_ID", nullable=false)
    private Music music;


    public Comment(String contents, User user, Music music){

        this.contents = contents;
        this.user = user;
        this.music = music;
    }

    public void update(String contents){

        this.contents = contents;
    }

}
