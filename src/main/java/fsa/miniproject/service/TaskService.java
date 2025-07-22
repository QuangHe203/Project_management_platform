package fsa.miniproject.service;

import fsa.miniproject.dao.TaskDao;
import fsa.miniproject.entity.Task;
import fsa.miniproject.entity.TaskStatusEnum;
import fsa.miniproject.dto.TaskDto;

import java.util.List;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public interface TaskService {
    boolean addTask(Task task);
    List<Task> getAllTasks();
    boolean updateTask(Task task);
    boolean updateTaskStatus(Integer taskId, TaskStatusEnum status);
    List<TaskDto> getTasksByStatus(String status);
    List<TaskDto> getAllTasksWithDetails();
    List<TaskDto> getTasksByStatusWithDetails(String status);
    List<Task> getTasksByTeamId(Integer teamId);
}
