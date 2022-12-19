package com.sparta.soundsea.comment.repository;

import com.sparta.soundsea.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByMusic_IdOrderByCreatedAtDesc(Long musicId);

}
