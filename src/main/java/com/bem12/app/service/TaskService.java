package com.bem12.app.service;

import com.bem12.app.dto.TaskRequest;
import com.bem12.app.entity.Task;
import com.bem12.app.repository.TaskRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class TaskService {

    private final TaskRepository repository;

    public TaskService(TaskRepository repository) {
        this.repository = repository;
    }

    @Cacheable(value = "tasks", key = "'all'")
    public List<Task> getAllTasks() {
        return repository.findAllByOrderByCompletedAscIdDesc();
    }

    @Cacheable(value = "tasks", key = "#id")
    public Task getTask(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Task not found: " + id));
    }

    @CacheEvict(value = "tasks", allEntries = true)
    public Task createTask(TaskRequest request) {
        Task task = new Task();
        task.setTitle(request.getTitle().strip());
        task.setDescription(request.getDescription() == null ? "" : request.getDescription().strip());
        task.setCompleted(Boolean.FALSE);
        return repository.save(task);
    }

    @Caching(evict = {
        @CacheEvict(value = "tasks", key = "#id"),
        @CacheEvict(value = "tasks", key = "'all'")
    })
    public Task updateTask(Long id, TaskRequest request) {
        Task task = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Task not found: " + id));
        task.setTitle(request.getTitle().strip());
        task.setDescription(request.getDescription() == null ? "" : request.getDescription().strip());
        task.setCompleted(request.getCompleted() != null ? request.getCompleted() : Boolean.FALSE);
        return repository.save(task);
    }

    @CacheEvict(value = "tasks", allEntries = true)
    public void deleteTask(Long id) {
        if (!repository.existsById(id)) {
            throw new NoSuchElementException("Task not found: " + id);
        }
        repository.deleteById(id);
    }
}
