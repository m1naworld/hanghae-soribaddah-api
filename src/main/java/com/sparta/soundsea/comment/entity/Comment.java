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
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String contents;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Music music;



}