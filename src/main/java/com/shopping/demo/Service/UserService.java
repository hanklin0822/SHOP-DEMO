package com.shopping.demo.Service;

import com.shopping.demo.DAO.UserRepository;
import com.shopping.demo.Entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

        public User getUserByUserName(String UserName) {
            return userRepository.findByUsername(UserName).orElse(null);
        }

    public String userRegister(User user) {

        Optional<User> existingUserOptional = userRepository.findByUsername(user.getUsername());

        if (existingUserOptional.isPresent()) {

            return "使用者已存在";

        } else {


            String encodedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encodedPassword);
            user.setAuthority("ROLE_USER");


            return "註冊成功";
        }
    }
}
