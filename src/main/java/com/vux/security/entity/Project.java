package com.vux.security.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Project {
    @Id
    @GeneratedValue
    private Integer id;

    @NotNull
    @Column(unique = true)
    private String name;

    @NotNull
    private LocalDate start;

    @Column(name = "expected_end")
    private LocalDate expectedEnd;

    private String description;

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<ProjectUser> projectUsers = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "project", cascade = CascadeType.ALL)
    private List<Comment> commentList = new LinkedList<>();
}
