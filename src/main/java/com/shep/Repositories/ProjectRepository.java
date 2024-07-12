package com.shep.Repositories;


import com.shep.Entities.Project;
import com.shep.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByUserId(Long userId);
    Project findProjectById(Long id);
}
