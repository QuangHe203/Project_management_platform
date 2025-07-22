package fsa.miniproject.dao;

import fsa.miniproject.dto.DetailUserDto;
import fsa.miniproject.dto.MemberUserDto;
import fsa.miniproject.dto.TeamUserDto;
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
    public Optional<DetailUserDto> findDetailByEmail(String email) {
        try {
            String jpql = "SELECT new fsa.miniproject.dto.DetailUserDto(" +
                    "u.accountId, u.role, u.name, u.email, u.password, t.id) " +
                    "FROM User u " +
                    "LEFT JOIN u.team t " +
                    "WHERE u.email = :email";


            DetailUserDto userDto = entityManager.createQuery(jpql, DetailUserDto.class)
                    .setParameter("email", email)
                    .getSingleResult();

            return Optional.of(userDto);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }



    @Override
    public List<MemberUserDto> findUsersByRole(RoleEnum role) {
        String jpql = "SELECT new fsa.miniproject.dto.MemberUserDto(u.accountId, u.name, u.email, u.role) FROM User u WHERE u.role = :role";

        return entityManager.createQuery(jpql, MemberUserDto.class)
                .setParameter("role", role)
                .getResultList();
    }

    @Override
    public Optional<User> findById(Integer userId) {
        try {
            String jpql = "FROM User u LEFT JOIN FETCH u.team WHERE u.accountId = :accountId";
            User user = entityManager.createQuery(jpql, User.class)
                    .setParameter("accountId", userId)
                    .getSingleResult();
            return Optional.of(user);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<TeamUserDto> findUsersByTeamId(Integer teamId) {
        String jpql = "SELECT new fsa.miniproject.dto.TeamUserDto(u.accountId, u.name, u.email, u.role) FROM User u WHERE u.team.teamId = :teamId";

        return entityManager.createQuery(jpql, TeamUserDto.class)
                .setParameter("teamId", teamId)
                .getResultList();
    }

}
