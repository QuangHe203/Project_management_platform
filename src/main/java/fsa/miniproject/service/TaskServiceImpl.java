package fsa.miniproject.service;

import fsa.miniproject.dao.TaskDao;
import fsa.miniproject.dto.TaskDto;
import fsa.miniproject.entity.Task;
import fsa.miniproject.entity.TaskStatusEnum;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class TaskServiceImpl implements TaskService {
    private final TaskDao taskDao;

    public TaskServiceImpl(TaskDao taskDao) {
        this.taskDao = taskDao;
    }

    @Override
    @Transactional
    public boolean addTask(Task task) {
        taskDao.save(task);
        return true;
    }

    @Override
    @Transactional
    public boolean updateTask(Task task) {
        taskDao.save(task);
        return true;
    }

    @Override
    @Transactional
    public boolean updateTaskStatus(Integer taskId, TaskStatusEnum status) {
        Optional<Task> optionalTask = taskDao.findById(taskId);
        if (optionalTask.isPresent()) {
            Task task = optionalTask.get();
            task.setStatus(status);
            taskDao.save(task);
            return true;
        }
        return false;
    }

    private TaskDto toDto(Task task) {
        return new TaskDto(
            task.getTaskId(),
            task.getTitle(),
            task.getContent(),
            task.getStatus(),
            task.getStartDate(),
            task.getEndDate(),
            task.getAssignee() != null ? task.getAssignee().getAccountId() : null,
            task.getAssignee() != null ? task.getAssignee().getName() : null
            // ,task.getProject() != null ? task.getProject().getProjectId() : null
            // ,task.getProject() != null ? task.getProject().getName() : null
        );
    }

    @Override
    public List<TaskDto> getTasksByStatus(String status) {
        List<Task> tasks = taskDao.findByStatus(status);
        List<TaskDto> dtos = new ArrayList<>();
        for (Task t : tasks) dtos.add(toDto(t));
        return dtos;
    }

    @Override
    public List<Task> getAllTasks() {
        return taskDao.findAll();
    }

    @Override
    public List<TaskDto> getAllTasksWithDetails() {
        List<Task> tasks = taskDao.findAllWithDetails();
        List<TaskDto> dtos = new ArrayList<>();
        for (Task t : tasks) dtos.add(toDto(t));
        return dtos;
    }

    @Override
    public List<TaskDto> getTasksByStatusWithDetails(String status) {
        List<Task> tasks = taskDao.findByStatusWithDetails(status);
        List<TaskDto> dtos = new ArrayList<>();
        for (Task t : tasks) dtos.add(toDto(t));
        return dtos;
    }

    @Override
    public List<Task> getTasksByTeamId(Integer teamId) {
        return taskDao.findByTeamId(teamId);
    }
}
