package com.Token.controllers;

import com.Token.models.Task;
import com.Token.services.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/list")
    public ResponseEntity<List<Task>> getUserTasks() {
        List<Task> tasks = taskService.getTasksForLoggedUser();
        return ResponseEntity.ok(tasks);
    }

    @PostMapping("/post")
    public ResponseEntity<?> createTask(@RequestBody Task task) {
        try{
        Task createdTask = taskService.createTask(task);
        return ResponseEntity.ok(createdTask);
        }
        catch(RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
