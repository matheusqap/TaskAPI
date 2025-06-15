package com.Token.controllers;

import com.Token.models.Task;
import com.Token.models.User;
import com.Token.services.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class TaskControllerTest {

    private TaskService taskService;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        taskService = Mockito.mock(TaskService.class);
        TaskController taskController = new TaskController(taskService);
        mockMvc = MockMvcBuilders.standaloneSetup(taskController).build();
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
    }

    @Test
    void testGetUserTasks() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");

        Task task1 = new Task(1L, "Task 1", "Description 1", "PENDING", user);
        Task task2 = new Task(2L, "Task 2", "Description 2", "COMPLETED", user);
        List<Task> tasks = Arrays.asList(task1, task2);

        when(taskService.getTasksForLoggedUser()).thenReturn(tasks);

        mockMvc.perform(get("/tasks/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].title").value("Task 1"))
                .andExpect(jsonPath("$[1].title").value("Task 2"));

        verify(taskService, times(1)).getTasksForLoggedUser();
    }

    @Test
    void testCreateTaskSuccess() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");

        Task inputTask = new Task(null, "New Task", "New Desc", "PENDING", null);
        Task createdTask = new Task(1L, "New Task", "New Desc", "PENDING", user);

        when(taskService.createTask(any(Task.class))).thenReturn(createdTask);

        mockMvc.perform(post("/tasks/post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputTask)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("New Task"))
                .andExpect(jsonPath("$.description").value("New Desc"))
                .andExpect(jsonPath("$.status").value("PENDING"));

        verify(taskService, times(1)).createTask(any(Task.class));
    }

    @Test
    void testCreateTaskFailure() throws Exception {
        Task inputTask = new Task(null, "New Task", "New Desc", "PENDING", null);

        when(taskService.createTask(any(Task.class)))
                .thenThrow(new RuntimeException("User not found"));

        mockMvc.perform(post("/tasks/post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputTask)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("User not found"));

        verify(taskService, times(1)).createTask(any(Task.class));
    }
}
