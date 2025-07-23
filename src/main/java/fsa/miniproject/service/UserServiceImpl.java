package fsa.miniproject.service;

import fsa.miniproject.dao.UserDao;
import fsa.miniproject.dto.*;
import fsa.miniproject.enums.RoleEnum;
import fsa.miniproject.entity.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }


    @Override
    public void registerUser(RegisterUserDto dto) {
        if (userDao.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email is already taken");
        }

        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(dto.getRole());

        userDao.save(user);
    }

    @Override
    public List<MemberUserDto> findUsersByRole(RoleEnum role) {
        System.out.println("Tìm kiếm người dùng với vai trò: " + role);
        List<MemberUserDto> users = userDao.findUsersByRole(role);
        System.out.println("Tìm thấy " + users.size() + " người dùng.");
        return users;

    }

    @Override
    public List<TeamUserDto> findUsersByTeamId(Integer teamId) {
        List<TeamUserDto> membersInSameTeam = userDao.findUsersByTeamId(teamId);
        membersInSameTeam.sort((u1, u2) -> {
            int roleCompare = Integer.compare(
                    u1.getRole() == RoleEnum.ROLE_MANAGER ? 0 : 1,
                    u2.getRole() == RoleEnum.ROLE_MANAGER ? 0 : 1
            );
            if (roleCompare != 0) return roleCompare;
            return u1.getName().compareToIgnoreCase(u2.getName());
        });
        return membersInSameTeam;
    }

    @Override
    public Optional<DetailUserDto> findDetailByEmail(String email) {
        return userDao.findDetailByEmail(email);
    }

    @Override
    public Optional<DetailUserDto> findDetailById(Integer id) {
        return userDao.findDetailById(id);
    }

    @Override
    public Optional<User> findById(Integer id) {
        return userDao.findById(id);
    }

}