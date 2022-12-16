package com.sparta.soundsea.comment.repository;

import com.sparta.soundsea.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {


}
