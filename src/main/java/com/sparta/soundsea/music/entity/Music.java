package com.sparta.soundsea.music.entity;

import com.sparta.soundsea.comment.entity.Comment;
import com.sparta.soundsea.common.entity.Timestamped;
import com.sparta.soundsea.user.entity.User;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
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

    @Column(nullable = false)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    @OneToMany(mappedBy = "comment", fetch = FetchType.LAZY)
    private List<Comment> comments = new ArrayList<>();





}
