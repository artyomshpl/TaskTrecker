package com.shep.Services;

import com.shep.Entities.Project;
import com.shep.Repositories.TaskRepository;
import com.shep.Entities.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;

    public Task save(Task task) {
        return taskRepository.save(task);
    }

    public void deleteById(Long id) {
        taskRepository.deleteById(id);
    }

    public Task findById(Long id) {
        return taskRepository.findTaskById(id); // Изменено название метода
    }
}
