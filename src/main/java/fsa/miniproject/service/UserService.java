package fsa.miniproject.service;

import fsa.miniproject.dto.RegisterUserDto;
import fsa.miniproject.entity.RoleEnum;
import fsa.miniproject.entity.User;

import java.util.ArrayList;
import java.util.List;

public interface UserService {
    void registerUser(RegisterUserDto dto);
    boolean authenticate(String email, String password);
    List<User> findUserByRole(RoleEnum role);
}
