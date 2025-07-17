package fsa.miniproject.service;

import fsa.miniproject.dao.TaskDao;
import fsa.miniproject.entity.Task;
import fsa.miniproject.entity.TaskStatusEnum;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Override
    public List<Task> getTasksByStatus(String status) {
        return taskDao.findByStatus(status);
    }

    @Override
    public List<Task> getAllTasks() {
        try {
            System.out.println("Getting all tasks...");
            List<Task> tasks = taskDao.findAll();
            System.out.println("Found " + tasks.size() + " tasks");
            return tasks;
        } catch (Exception e) {
            System.out.println("Error getting tasks: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
