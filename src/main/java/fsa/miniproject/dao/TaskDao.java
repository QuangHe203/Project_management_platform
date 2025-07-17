package fsa.miniproject.dao;

import fsa.miniproject.entity.Task;

import java.util.List;
import java.util.Optional;

public interface TaskDao {
    Optional<Task> save(Task task);
    List<Task> findAll();
    Optional<Task> findById(Integer id);
    List<Task> findByStatus(String status);
}
