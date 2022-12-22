package com.sparta.soundsea.music.entity;

import com.sparta.soundsea.comment.entity.Comment;
import com.sparta.soundsea.common.entity.Timestamped;
import com.sparta.soundsea.music.dto.RequestCreateMusic;
import com.sparta.soundsea.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Music extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String contents;

    @Column(nullable = false)
    private String artist;

    @Column(nullable = false)
    private String image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;

    @OneToMany(mappedBy = "music", fetch = FetchType.LAZY)
    private List<Comment> comments = new ArrayList<>();


    public Music(String title, String contents, String artist, String image, User user){
        this.title = title;
        this.contents = contents;
        this.artist = artist;
        this.image = image;
        this.user = user;
    }


    public void updateMusic(RequestCreateMusic requestDto) {

        this.title = requestDto.getTitle();
        this.contents = requestDto.getContents();
        this.artist = requestDto.getArtist();
        this.image = requestDto.getImage();
    }
}
