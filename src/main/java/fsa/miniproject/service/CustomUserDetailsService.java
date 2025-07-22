package fsa.miniproject.service;

import fsa.miniproject.dao.UserDao;
import fsa.miniproject.dto.DetailUserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("userDetailsService")
public class CustomUserDetailsService implements UserDetailsService {

    private final UserDao userDao;

    @Autowired
    public CustomUserDetailsService(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println("Loading user by email: " + email);
        DetailUserDto user = userDao.findDetailByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));
        System.out.println("User found: " + user.getEmail() + ", Role: " + user.getRole());
        return new CustomUserDetails(user);
    }
}