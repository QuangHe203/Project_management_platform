package fsa.miniproject.dao;

import fsa.miniproject.dto.DetailTaskDto;
import fsa.miniproject.entity.Task;
import fsa.miniproject.enums.TaskStatusEnum;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class TaskDaoImpl implements TaskDao {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Task> save(Task task) {
        if (task.getTaskId() == null) {
            entityManager.persist(task);
            return Optional.of(task);
        } else {
            Task updated = entityManager.merge(task);
            return Optional.of(updated);
        }
    }

    @Override
    public List<DetailTaskDto> findAll() {
        String jpql = "SELECT new fsa.miniproject.dto.DetailTaskDto(" +
                "t.taskId, t.title, t.content, t.status, t.startDate, t.endDate, t.assignee.accountId, t.assignee.name, t.assignee.role) " +
                "FROM Task t";

        return entityManager.createQuery(jpql, DetailTaskDto.class).getResultList();
    }

    @Override
    public Optional<DetailTaskDto> findDetailById(Integer taskId) {
        String jpql = "SELECT new fsa.miniproject.dto.DetailTaskDto(" +
                "t.taskId, t.title, t.content, t.status, t.startDate, t.endDate, t.assignee.accountId, t.assignee.name, t.assignee.role) " +
                "FROM Task t WHERE t.taskId = :taskId";

        DetailTaskDto dto = entityManager.createQuery(jpql, DetailTaskDto.class)
                .setParameter("taskId", taskId)
                .getSingleResult();
        return Optional.of(dto);
    }

    @Override
    public Optional<Task> findTaskById(Integer id) {
        String jpql = "FROM Task t WHERE t.taskId = :id";
        List<Task> result = entityManager.createQuery(jpql, Task.class)
                .setParameter("id", id)
                .getResultList();

        return result.stream().findFirst();
    }

    @Override
    public List<DetailTaskDto> findAllWithDetails() {
        String jpql = "SELECT new fsa.miniproject.dto.DetailTaskDto(" +
                "t.taskId, t.title, t.content, t.status, t.startDate, t.endDate, t.assignee.accountId, t.assignee.name, t.assignee.role) " +
                "FROM Task t";  // Cập nhật để lấy thêm chi tiết

        return entityManager.createQuery(jpql, DetailTaskDto.class).getResultList();
    }

    @Override
    public List<DetailTaskDto> findByStatusWithDetails(String status) {
        String jpql = "SELECT new fsa.miniproject.dto.DetailTaskDto(" +
                "t.taskId, t.title, t.content, t.status, t.startDate, t.endDate, t.assignee.accountId, t.assignee.name, t.assignee.role) " +
                "FROM Task t WHERE t.status = :status";

        return entityManager.createQuery(jpql, DetailTaskDto.class)
                .setParameter("status", TaskStatusEnum.valueOf(status))
                .getResultList();
    }

    @Override
    public List<DetailTaskDto> findByTeamId(Integer teamId) {
        String jpql = "SELECT new fsa.miniproject.dto.DetailTaskDto(" +
                "t.taskId, t.title, t.content, t.status, t.startDate, t.endDate, t.assignee.accountId, t.assignee.name, t.assignee.role) " +
                "FROM Task t WHERE t.assignee.team.teamId = :teamId";

        return entityManager.createQuery(jpql, DetailTaskDto.class)
                .setParameter("teamId", teamId)
                .getResultList();
    }

    @Override
    public List<DetailTaskDto> findbyUserId(Integer userId) {
        return null;
    }

    @Override
    public void deleteById(Integer id) {
        Task task = entityManager.find(Task.class, id);
        if (task != null) {
            entityManager.remove(task);
        }
    }
}
