package fsa.miniproject.dao;

import fsa.miniproject.entity.Task;
import fsa.miniproject.entity.TaskStatusEnum;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class TaskDaoImpl implements TaskDao {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public Optional<Task> save(Task task) {
        Task mergedTask = entityManager.merge(task);
        return Optional.of(mergedTask);
    }

    @Override
    public List<Task> findAll() {
        return entityManager.createQuery("FROM Task", Task.class).getResultList();
    }

    @Override
    public Optional<Task> findById(Integer id) {
        return Optional.ofNullable(entityManager.find(Task.class, id));
    }

    @Override
    public List<Task> findByStatus(String status) {
        return entityManager.createQuery("FROM Task WHERE status = :status", Task.class)
                .setParameter("status", status)
                .getResultList();
    }

    @Override
    public List<Task> findAllWithDetails() {
        // Tạm thời chỉ lấy tất cả Task, không join project
        return entityManager.createQuery("FROM Task t", Task.class).getResultList();
    }

    @Override
    public List<Task> findByStatusWithDetails(String status) {
        // Tạm thời chỉ lấy Task theo status, không join project
        return entityManager.createQuery("FROM Task t WHERE t.status = :status", Task.class)
                .setParameter("status", TaskStatusEnum.valueOf(status))
                .getResultList();
    }

    @Override
    public List<Task> findByTeamId(Integer teamId) {
        String jpql = "SELECT t FROM Task t WHERE t.assignee.team.teamId = :teamId";

        return entityManager.createQuery(jpql, Task.class)
                .setParameter("teamId", teamId)
                .getResultList();
    }
}
