package com.shep.Controllers;

import com.shep.Entities.Project;
import com.shep.Entities.User;
import com.shep.Services.ProjectService;
import com.shep.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {
    @Autowired
    private ProjectService projectService;
    @Autowired
    private UserService userService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<?> createProject(@RequestBody Project project) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();
        User currentUser = userService.findByUsername(currentUserName);
        project.setUserId(currentUser.getId());
        Project savedProject = projectService.save(project);
        return ResponseEntity.ok(savedProject);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping
    public ResponseEntity<List<Project>> getAllProjects(Authentication authentication) {
        String currentUserName = authentication.getName();
        User currentUser = userService.findByUsername(currentUserName);
        List<Project> projects = projectService.getProjectsByUserId(currentUser.getId());
        return ResponseEntity.ok(projects);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<Project> addProject(@RequestBody Project project) {
        Project newProject = projectService.save(project);
        return ResponseEntity.ok(newProject);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update/{id}")
    public ResponseEntity<Project> updateProject(@PathVariable Long id, @RequestBody Project updatedProject) {
        Project existingProject = projectService.findById(id);
        if (existingProject == null) {
            return ResponseEntity.notFound().build();
        }
        existingProject.setName(updatedProject.getName());
        existingProject.setDescription(updatedProject.getDescription());
        Project savedProject = projectService.save(existingProject);
        return ResponseEntity.ok(savedProject);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        Project existingProject = projectService.findById(id);
        if (existingProject == null) {
            return ResponseEntity.notFound().build();
        }
        projectService.deleteById(id);
        return ResponseEntity.noContent().build();
    }



}
