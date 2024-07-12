package com.shep.Controllers;

import com.shep.Entities.Project;
import com.shep.Entities.Task;
import com.shep.Services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    @Autowired
    private TaskService taskService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<?> createTask(@RequestBody Task task) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();
        // You can fetch the current project based on the user input or any other logic
        Project project = new Project(); // Replace this with the actual project object
        task.setProject(project);
        Task savedTask = taskService.save(task);
        return ResponseEntity.ok(savedTask);
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/add")
    public ResponseEntity<Task> addTask(@RequestBody Task task) {
        Task newTask = taskService.save(task);
        return ResponseEntity.ok(newTask);
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/update/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody Task updatedTask) {
        Task existingTask = taskService.findById(id);
        if (existingTask == null) {
            return ResponseEntity.notFound().build();
        }
        existingTask.setTitle(updatedTask.getTitle());
        existingTask.setDescription(updatedTask.getDescription());
        existingTask.setCompleted(updatedTask.isCompleted());
        existingTask.setTimeSpent(updatedTask.getTimeSpent());
        Task savedTask = taskService.save(existingTask);
        return ResponseEntity.ok(savedTask);
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        Task existingTask = taskService.findById(id);
        if (existingTask == null) {
            return ResponseEntity.notFound().build();
        }
        taskService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
