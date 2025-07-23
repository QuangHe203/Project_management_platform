package fsa.miniproject.service;

import fsa.miniproject.dto.UpdateTaskDto;
import fsa.miniproject.entity.Task;
import fsa.miniproject.dto.DetailTaskDto;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public interface TaskService {
    Optional<DetailTaskDto> getById(Integer id);
    boolean addTask(Task task);
    boolean updateTask(Task task);
    Optional<Task> updateFromDto(UpdateTaskDto dto);
    List<DetailTaskDto> getAllTasksWithDetails();
    List<DetailTaskDto> getTasksByStatusWithDetails(String status);
    List<DetailTaskDto> getTasksByTeamId(Integer teamId);
    boolean deleteTask(Integer id);
}
