package fsa.miniproject.service;

import fsa.miniproject.dto.*;
import fsa.miniproject.enums.RoleEnum;
import fsa.miniproject.entity.User;


import java.util.List;
import java.util.Optional;

public interface UserService {
    void registerUser(RegisterUserDto dto);
    Optional<DetailUserDto> findDetailByEmail(String email);
    List<MemberUserDto> findUsersByRole(RoleEnum role);
    List<TeamUserDto> findUsersByTeamId(Integer teamId);
    Optional<DetailUserDto> findDetailById(Integer id);
    Optional<User> findById(Integer id);
}
