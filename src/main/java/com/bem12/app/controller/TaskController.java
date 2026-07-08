package com.bem12.app.controller;

import com.bem12.app.dto.TaskRequest;
import com.bem12.app.entity.Task;
import com.bem12.app.service.TaskService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.NoSuchElementException;

@Controller
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/")
    public String root() {
        return "redirect:/tasks";
    }

    @GetMapping("/tasks")
    public String listTasks(Model model) {
        model.addAttribute("tasks", taskService.getAllTasks());
        model.addAttribute("newTask", new TaskRequest());
        return "index";
    }

    @PostMapping("/tasks")
    public String createTask(@ModelAttribute TaskRequest request, RedirectAttributes ra) {
        taskService.createTask(request);
        ra.addFlashAttribute("message", "Task created successfully.");
        return "redirect:/tasks";
    }

    @GetMapping("/tasks/{id}/edit")
    public String editForm(@PathVariable Long id, Model model, RedirectAttributes ra) {
        try {
            Task task = taskService.getTask(id);
            model.addAttribute("task", new TaskRequest(task.getTitle(), task.getDescription(), task.getCompleted()));
            model.addAttribute("id", id);
            return "edit";
        } catch (NoSuchElementException e) {
            ra.addFlashAttribute("error", "Task not found.");
            return "redirect:/tasks";
        }
    }

    @PostMapping("/tasks/{id}")
    public String updateTask(@PathVariable Long id, @ModelAttribute TaskRequest request, RedirectAttributes ra) {
        try {
            taskService.updateTask(id, request);
            ra.addFlashAttribute("message", "Task updated successfully.");
        } catch (NoSuchElementException e) {
            ra.addFlashAttribute("error", "Task not found.");
        }
        return "redirect:/tasks";
    }

    @PostMapping("/tasks/{id}/delete")
    public String deleteTask(@PathVariable Long id, RedirectAttributes ra) {
        try {
            taskService.deleteTask(id);
            ra.addFlashAttribute("message", "Task deleted.");
        } catch (NoSuchElementException e) {
            ra.addFlashAttribute("error", "Task not found.");
        }
        return "redirect:/tasks";
    }
}
