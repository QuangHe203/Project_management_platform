package fsa.miniproject.service;

import fsa.miniproject.dto.RegisterUserDto;
import fsa.miniproject.entity.RoleEnum;
import fsa.miniproject.entity.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface UserService {
    void registerUser(RegisterUserDto dto);
    boolean authenticate(String email, String password);
    Optional<User> findByEmail(String email);
    List<User> findUserByRole(RoleEnum role);
    List<User> findUsersByTeamId(Integer teamId);
    Optional<User> findById(Integer id);
}
