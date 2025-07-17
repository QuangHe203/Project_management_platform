package fsa.miniproject.dao;

import fsa.miniproject.entity.Team;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class TeamDaoImpl implements TeamDao{

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public boolean save(Team team) {
        entityManager.persist(team);
        return true;
    }

    @Override
    public Optional<Team> findById(Integer id) {
        return Optional.ofNullable(entityManager.find(Team.class, id));
    }
}
