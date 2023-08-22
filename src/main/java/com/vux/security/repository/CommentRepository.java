package com.vux.security.repository;

import com.vux.security.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    @Query(value = "select c from Comment c where c.user.id = ?1")
    List<Comment> findByUserId(Integer userId);

    @Query(value = "select c from Comment c where c.project.id = ?1")
    List<Comment> findByProjectId(Integer projectId);


    @Query(value = "select c from Comment  c where c.createdAt between ?1 and ?2")
    List<Comment> findBetwenDuration(LocalDate start, LocalDate end);
}
