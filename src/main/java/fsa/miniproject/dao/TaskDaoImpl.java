package fsa.miniproject.dao;

import fsa.miniproject.entity.Task;
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
        entityManager.persist(task);
        return Optional.of(task);
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
}
