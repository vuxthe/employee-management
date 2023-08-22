package com.vux.security.controller.admin;

import com.vux.security.dto.CommentDto;
import com.vux.security.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/comment")
@RequiredArgsConstructor
public class CommentControllerAdmin {
    private final CommentService commentService;

    @GetMapping("/project/{projectId}")
    public List<CommentDto> searchByProject(
            @PathVariable Integer projectId
    ) {
        return commentService.getCommentsByProject(projectId);
    }

    @GetMapping("/user/{userId}")
    public List<CommentDto> searchByUser(
            @PathVariable Integer userId
    ) {
        return commentService.getComments(userId);
    }

    @GetMapping
    public List<CommentDto> searchByDuration(
            @RequestParam(required = false) LocalDate start,
            @RequestParam(required = false) LocalDate end
    ) {
        if (start == null || end == null) {
            return commentService.getAll();
        }
        return commentService.getCommentBetweenDuration(start, end);
    }
}
