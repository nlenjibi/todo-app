package com.bem12.app.repository;

import com.bem12.app.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findAllByOrderByCompletedAscIdDesc();
}
