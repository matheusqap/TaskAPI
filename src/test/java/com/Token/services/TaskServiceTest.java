package com.Token.services;

import com.Token.models.Task;
import com.Token.models.User;
import com.Token.repositories.TaskRepository;
import com.Token.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TaskServiceTest {

    private TaskRepository taskRepository;
    private UserRepository userRepository;
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskRepository = Mockito.mock(TaskRepository.class);
        userRepository = Mockito.mock(UserRepository.class);
        taskService = new TaskService(taskRepository, userRepository);
    }

    @Test
    void testGetTasksForLoggedUser() {
        // Simulando usuário logado
        String username = "testuser";
        User user = new User();
        user.setId(1L);
        user.setUsername(username);

        Task task1 = new Task(1L, "Task 1", "Description 1", "PENDING", user);
        Task task2 = new Task(2L, "Task 2", "Description 2", "COMPLETED", user);

        // Mock do contexto de autenticação
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(username);

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        try (MockedStatic<SecurityContextHolder> mocked = Mockito.mockStatic(SecurityContextHolder.class)) {
            mocked.when(SecurityContextHolder::getContext).thenReturn(securityContext);

            // Mock do userRepository
            when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

            // Mock do taskRepository
            when(taskRepository.findByUser(user)).thenReturn(Arrays.asList(task1, task2));

            // Executa o método
            List<Task> tasks = taskService.getTasksForLoggedUser();

            // Verificações
            assertEquals(2, tasks.size());
            assertEquals("Task 1", tasks.get(0).getTitle());
            verify(userRepository, times(1)).findByUsername(username);
            verify(taskRepository, times(1)).findByUser(user);
        }
    }

    @Test
    void testCreateTask() {
        // Simulando usuário logado
        String username = "testuser";
        User user = new User();
        user.setId(1L);
        user.setUsername(username);

        Task inputTask = new Task(null, "New Task", "New Desc", "PENDING", null);
        Task savedTask = new Task(1L, "New Task", "New Desc", "PENDING", user);

        // Mock do contexto de autenticação
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(username);

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        try (MockedStatic<SecurityContextHolder> mocked = Mockito.mockStatic(SecurityContextHolder.class)) {
            mocked.when(SecurityContextHolder::getContext).thenReturn(securityContext);

            // Mock do userRepository
            when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

            // Mock do taskRepository
            when(taskRepository.save(any(Task.class))).thenReturn(savedTask);

            // Executa o método
            Task createdTask = taskService.createTask(inputTask);

            // Verificações
            assertNotNull(createdTask);
            assertEquals("New Task", createdTask.getTitle());
            assertEquals(user, createdTask.getUser());
            verify(userRepository, times(1)).findByUsername(username);
            verify(taskRepository, times(1)).save(inputTask);
        }
    }
}
