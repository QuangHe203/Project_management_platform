package fsa.miniproject.service;

import fsa.miniproject.dao.UserDao;
import fsa.miniproject.dto.RegisterUserDto;
import fsa.miniproject.entity.RoleEnum;
import org.springframework.beans.factory.annotation.Autowired;
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
    public boolean authenticate(String email, String password) {
        Optional<User> optionalUser = userDao.findByEmail(email);

        if (!optionalUser.isPresent()) {
            return false;
        }

        User user = optionalUser.get();
        return passwordEncoder.matches(password, user.getPassword());
    }

    @Override
    public List<User> findUserByRole(RoleEnum role) {
        System.out.println("Tìm kiếm người dùng với vai trò: " + role);
        List<User> users = userDao.findUserByRole(role);
        System.out.println("Tìm thấy " + users.size() + " người dùng.");
        return users;

    }
}