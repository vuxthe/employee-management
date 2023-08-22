package com.vux.security.repository;

import com.vux.security.entity.ProjectUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProjectUserRepository extends JpaRepository<ProjectUser, Integer> {
    @Query(value = "delete from ProjectUser where project = ?1 and user = ?2")
    void removeUserInProject(Integer projectId, Integer userId);
}
