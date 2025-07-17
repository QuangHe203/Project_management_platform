package fsa.miniproject.dao;

import fsa.miniproject.entity.RoleEnum;
import fsa.miniproject.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    void save(User user);
    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
    List<User> findUserByRole(RoleEnum role);
    Optional<User> findById(Integer id);
}