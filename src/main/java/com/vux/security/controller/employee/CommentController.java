package com.vux.security.controller.employee;

import com.vux.security.entity.Comment;
import com.vux.security.repository.UserRepository;
import com.vux.security.dto.CommentDto;
import com.vux.security.entity.User;
import com.vux.security.payload.CommentRequest;
import com.vux.security.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/employee/comment")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;
    private final UserRepository userRepository;

    @GetMapping("")
    public List<CommentDto> getComments(
    ) throws ChangeSetPersister.NotFoundException {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }

        User user = userRepository.findByEmail(username).orElseThrow(ChangeSetPersister.NotFoundException::new);
        return commentService.getComments(user.getId());
    }

    @PostMapping("")
    public Comment create(
            @RequestBody CommentRequest request
    ) throws ChangeSetPersister.NotFoundException {
        return commentService.create(request);
    }

    @PutMapping("")
    public Comment update(
            @RequestBody CommentRequest request,
            @RequestParam Integer commentId
    ) throws ChangeSetPersister.NotFoundException {
        return  commentService.update(commentId, request);
    }

    @DeleteMapping
    public void delete(
            @RequestParam Integer commentId
    ) {
        commentService.detete(commentId);
    }


}
