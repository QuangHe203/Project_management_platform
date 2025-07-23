package fsa.miniproject.service;

import fsa.miniproject.dao.TaskDao;
import fsa.miniproject.dto.DetailTaskDto;
import fsa.miniproject.dto.UpdateTaskDto;
import fsa.miniproject.entity.Task;
import fsa.miniproject.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public Optional<DetailTaskDto> getById(Integer id) {
        return taskDao.findDetailById(id);
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
    public Optional<Task> updateFromDto(UpdateTaskDto dto) {
        Optional<Task> optionalTask = taskDao.findTaskById(dto.getTaskId());

        if (!optionalTask.isPresent()) {
            return Optional.empty();
        }

        Task task = optionalTask.get();

        task.setTitle(dto.getTitle());
        task.setContent(dto.getContent());
        task.setStartDate(dto.getStartDate());
        task.setEndDate(dto.getEndDate());

        if (dto.getAssignee() != null) {
            User assignee = new User();
            assignee.setAccountId(dto.getAssignee());
            task.setAssignee(assignee);
        } else {
            task.setAssignee(null);
        }

        taskDao.save(task);

        return Optional.of(task);
    }

    @Override
    public List<DetailTaskDto> getAllTasksWithDetails() {
        return taskDao.findAllWithDetails();
    }

    @Override
    public List<DetailTaskDto> getTasksByStatusWithDetails(String status) {
        return taskDao.findByStatusWithDetails(status);
    }

    @Override
    public List<DetailTaskDto> getTasksByTeamId(Integer teamId) {
        return taskDao.findByTeamId(teamId); // Trả về danh sách Task Entity
    }

    @Override
    @Transactional
    public boolean deleteTask(Integer id) {
        Optional<Task> optionalTask = taskDao.findTaskById(id);
        if (optionalTask.isPresent()) {
            taskDao.deleteById(id);
            return true;
        }
        return false;
    }
}
