package fsa.miniproject.dao;

import fsa.miniproject.dto.DetailTaskDto;
import fsa.miniproject.dto.UpdateTaskDto;
import fsa.miniproject.entity.Task;

import java.util.List;
import java.util.Optional;

public interface TaskDao {
    Optional<Task> save(Task task);
    List<DetailTaskDto> findAll();
    Optional<DetailTaskDto> findDetailById(Integer id);
    Optional<Task> findTaskById(Integer id);
    void deleteById(Integer id);
    List<DetailTaskDto> findAllWithDetails();
    List<DetailTaskDto> findByStatusWithDetails(String status);
    List<DetailTaskDto> findByTeamId(Integer teamId);
    List<DetailTaskDto> findbyUserId(Integer userId);
}
