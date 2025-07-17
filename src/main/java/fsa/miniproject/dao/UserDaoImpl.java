package fsa.miniproject.dao;

import fsa.miniproject.entity.RoleEnum;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import fsa.miniproject.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserDaoImpl implements UserDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public void save(User user) {
        entityManager.persist(user);
    }

    @Override
    public boolean existsByEmail(String email) {
        String jpql = "SELECT COUNT(u) FROM User u WHERE u.email = :email";
        Long count = entityManager.createQuery(jpql, Long.class)
                .setParameter("email", email)
                .getSingleResult();
        return count > 0;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        try {
            String jpql = "FROM User u WHERE u.email = :email";
            User user = entityManager.createQuery(jpql, User.class)
                    .setParameter("email", email)
                    .getSingleResult();
            return Optional.of(user);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<User> findUserByRole(RoleEnum role) {
        String jpql = "FROM User u WHERE u.role = :role";
        return entityManager.createQuery(jpql, User.class)
                .setParameter("role", role)
                .getResultList();
    }

    @Override
    public Optional<User> findById(Integer userId) {
        try {
            String jpql = "FROM User u WHERE u.accountId = :accountId";
            User user = entityManager.createQuery(jpql, User.class)
                    .setParameter("accountId", userId)
                    .getSingleResult();
            return Optional.of(user);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
}
