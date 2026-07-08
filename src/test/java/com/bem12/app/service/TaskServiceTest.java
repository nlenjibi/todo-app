package com.bem12.app.service;

import com.bem12.app.dto.TaskRequest;
import com.bem12.app.entity.Task;
import com.bem12.app.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository repository;

    @InjectMocks
    private TaskService taskService;

    private Task task;

    @BeforeEach
    void setUp() {
        task = new Task();
        task.setId(1L);
        task.setTitle("Test Task");
        task.setDescription("Description");
        task.setCompleted(false);
    }

    @Test
    void getAllTasks_returnsListFromRepository() {
        when(repository.findAllByOrderByCompletedAscIdDesc()).thenReturn(List.of(task));
        List<Task> result = taskService.getAllTasks();
        assertThat(result).hasSize(1).contains(task);
    }

    @Test
    void getTask_returnTask_whenFound() {
        when(repository.findById(1L)).thenReturn(Optional.of(task));
        Task result = taskService.getTask(1L);
        assertThat(result.getTitle()).isEqualTo("Test Task");
    }

    @Test
    void getTask_throwsNotFound_whenMissing() {
        when(repository.findById(99L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> taskService.getTask(99L))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void createTask_savesAndReturnsTask() {
        TaskRequest request = new TaskRequest("New Task", "Desc", null);
        when(repository.save(any(Task.class))).thenReturn(task);
        Task result = taskService.createTask(request);
        verify(repository).save(any(Task.class));
        assertThat(result).isNotNull();
    }

    @Test
    void updateTask_updatesFields() {
        TaskRequest request = new TaskRequest("Updated", "New Desc", true);
        when(repository.findById(1L)).thenReturn(Optional.of(task));
        when(repository.save(any(Task.class))).thenReturn(task);
        taskService.updateTask(1L, request);
        verify(repository).save(task);
        assertThat(task.getTitle()).isEqualTo("Updated");
        assertThat(task.getCompleted()).isTrue();
    }

    @Test
    void deleteTask_deletesById() {
        when(repository.existsById(1L)).thenReturn(true);
        taskService.deleteTask(1L);
        verify(repository).deleteById(1L);
    }

    @Test
    void deleteTask_throwsNotFound_whenMissing() {
        when(repository.existsById(99L)).thenReturn(false);
        assertThatThrownBy(() -> taskService.deleteTask(99L))
                .isInstanceOf(NoSuchElementException.class);
    }
}
