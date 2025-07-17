package fsa.miniproject.service;

import fsa.miniproject.dao.TaskDao;
import fsa.miniproject.entity.Task;
import fsa.miniproject.entity.TaskStatusEnum;

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
    List<Task> getTasksByStatus(String status);
}
