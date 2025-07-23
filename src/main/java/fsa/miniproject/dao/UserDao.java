package fsa.miniproject.dao;

import fsa.miniproject.dto.DetailUserDto;
import fsa.miniproject.dto.MemberUserDto;
import fsa.miniproject.dto.TeamUserDto;
import fsa.miniproject.enums.RoleEnum;
import fsa.miniproject.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    void save(User user);
    boolean existsByEmail(String email);
    Optional<DetailUserDto> findDetailByEmail(String email);
    List<MemberUserDto> findUsersByRole(RoleEnum role);
    Optional<User> findById(Integer id);
    Optional<DetailUserDto> findDetailById(Integer id);
    List<TeamUserDto> findUsersByTeamId(Integer teamId);
}