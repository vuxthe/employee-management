package com.vux.security.service.impl;

import com.vux.security.entity.Comment;
import com.vux.security.entity.Project;
import com.vux.security.entity.User;
import com.vux.security.mapper.MapStructMapper;
import com.vux.security.payload.CommentRequest;
import com.vux.security.repository.CommentRepository;
import com.vux.security.repository.ProjectRepository;
import com.vux.security.repository.UserRepository;
import com.vux.security.dto.CommentDto;
import com.vux.security.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository repository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final MapStructMapper mapper;
    @Override
    public Comment create(CommentRequest request) throws ChangeSetPersister.NotFoundException {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(ChangeSetPersister.NotFoundException::new);
        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(ChangeSetPersister.NotFoundException::new);

        Comment entity = Comment.builder()
                .createdAt(LocalDateTime.now())
                .content(request.getContent())
                .user(user)
                .project(project)
                .build();
        return repository.save(entity);
    }

    @Override
    public Comment update(Integer commentId, CommentRequest request) throws ChangeSetPersister.NotFoundException {
        Comment entity = repository.findById(commentId)
                .orElseThrow(ChangeSetPersister.NotFoundException::new);
        entity.setContent(request.getContent());
        entity.setEditedAt(LocalDateTime.now());
        return repository.save(entity);
    }

    @Override
    public List<CommentDto> getComments(Integer userId) {
        List<Comment> commentList = repository.findByUserId(userId);
        return commentList.stream().map(
                mapper::commentToCommentDto
        ).collect(Collectors.toList());
    }

    @Override
    public List<CommentDto> getCommentsByProject(Integer projectId) {
        List<Comment> commentList = repository.findByProjectId(projectId);
        return commentList.stream().map(
                mapper::commentToCommentDto
        ).collect(Collectors.toList());
    }

    @Override
    public List<CommentDto> getAll() {
        return repository.findAll().stream().map(
                mapper::commentToCommentDto
        ).collect(Collectors.toList());
    }

    @Override
    public List<CommentDto> getCommentBetweenDuration(LocalDate start, LocalDate end) {
        return repository.findBetwenDuration(start, end)
                .stream().map(mapper::commentToCommentDto).collect(Collectors.toList());
    }

    @Override
    public void detete(Integer commentId) {
        repository.deleteById(commentId);
    }
}
