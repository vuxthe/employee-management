package com.vux.security.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
public class ProjectDto {
    private String name;
    private LocalDate start;
    private LocalDate end;
    private String description;
    private List<String> commentList;
}
