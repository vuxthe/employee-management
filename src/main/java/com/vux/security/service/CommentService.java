package com.vux.security.service;

import com.vux.security.entity.Comment;
import com.vux.security.payload.CommentRequest;
import com.vux.security.dto.CommentDto;
import jakarta.transaction.Transactional;
import org.springframework.data.crossstore.ChangeSetPersister;

import java.time.LocalDate;
import java.util.List;

@Transactional(rollbackOn = {ChangeSetPersister.NotFoundException.class})
public interface CommentService {

    Comment create(CommentRequest request) throws ChangeSetPersister.NotFoundException;
    Comment update(Integer commentId, CommentRequest request) throws ChangeSetPersister.NotFoundException;

    List<CommentDto> getComments(Integer userId);
    List<CommentDto> getCommentsByProject(Integer projectId);
    List<CommentDto> getAll();

    List<CommentDto> getCommentBetweenDuration(LocalDate start, LocalDate end);

    void detete(Integer commentId);
}
