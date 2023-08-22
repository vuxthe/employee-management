package com.vux.security.mapper;

import com.vux.security.dto.ProjectDto;
import com.vux.security.entity.Project;
import com.vux.security.dto.CommentDto;
import com.vux.security.entity.Comment;
import com.vux.security.dto.CheckStatusDto;
import com.vux.security.dto.UserDto;
import com.vux.security.entity.User;
import org.springframework.stereotype.Component;

import com.vux.security.entity.CheckStatus;

@Component
public class MapStructMapper {

	public CommentDto commentToCommentDto(Comment comment) {
		return CommentDto.builder()
				.content(comment.getContent())
				.createdAt(comment.getCreatedAt())
				.editedAt(comment.getEditedAt())
				.userId(comment.getUser().getId())
				.projectId(comment.getProject().getId())
				.build();
	}
}
