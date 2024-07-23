package com.shep.controllers;

import com.shep.entities.Project;
import com.shep.services.ProjectService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @GetMapping("/allProjects")
    public ResponseEntity<List<Project>> getAllProjects() {
        List<Project> projects = projectService.getProjects();
        return ResponseEntity.ok(projects);
    }

    @GetMapping
    public ResponseEntity<Project> getProjectById(@RequestParam("id") int id) {
        return ResponseEntity.ok(projectService.getProject((long) id));
    }

    @PostMapping("/createProject")
//    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Project> createProject(@RequestBody Project project, HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        System.out.println("Authorization header: " + authorizationHeader);
        return ResponseEntity.ok(projectService.saveProject(project));
    }
}

