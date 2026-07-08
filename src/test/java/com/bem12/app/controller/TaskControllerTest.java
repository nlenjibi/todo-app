package com.bem12.app.controller;

import com.bem12.app.dto.TaskRequest;
import com.bem12.app.entity.Task;
import com.bem12.app.service.TaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(TaskController.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @Test
    void root_redirectsToTasks() throws Exception {
        mockMvc.perform(get("/"))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("/tasks"));
    }

    @Test
    void listTasks_returnsIndexView() throws Exception {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Test");
        task.setCompleted(false);
        when(taskService.getAllTasks()).thenReturn(List.of(task));

        mockMvc.perform(get("/tasks"))
               .andExpect(status().isOk())
               .andExpect(view().name("index"))
               .andExpect(model().attributeExists("tasks", "newTask"));
    }

    @Test
    void createTask_redirectsAfterCreate() throws Exception {
        Task created = new Task();
        created.setId(2L);
        when(taskService.createTask(any(TaskRequest.class))).thenReturn(created);

        mockMvc.perform(post("/tasks")
                       .param("title", "New Task")
                       .param("description", "Some description"))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("/tasks"));

        verify(taskService).createTask(any(TaskRequest.class));
    }

    @Test
    void updateTask_redirectsAfterUpdate() throws Exception {
        Task updated = new Task();
        updated.setId(1L);
        when(taskService.updateTask(eq(1L), any(TaskRequest.class))).thenReturn(updated);

        mockMvc.perform(post("/tasks/1")
                       .param("title", "Updated Title")
                       .param("description", "")
                       .param("completed", "true"))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("/tasks"));
    }

    @Test
    void deleteTask_redirectsAfterDelete() throws Exception {
        mockMvc.perform(post("/tasks/1/delete"))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("/tasks"));

        verify(taskService).deleteTask(1L);
    }
}
