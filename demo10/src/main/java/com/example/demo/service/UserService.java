package com.example.demo.service;

import com.example.demo.models.Book;
import com.example.demo.models.User;
import com.example.demo.models.enums.Role;
import com.example.demo.rep.BookRepository;
import com.example.demo.rep.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final BookRepository bookRepository;

    public boolean createUser(User user) {
        String email = user.getName();
        if (userRepository.findByEmail(email) != null) return false;
        user.setActive(true);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.getRoles().add(Role.ROLE_USER);
        if (userRepository.existsByEmail(user.getEmail())) {
            return false;
        }
        userRepository.save(user);
        return true;
    }

    /*    public void banUser(Long id) {
            User user =userRepository.findById(id).orElse(null);
            if(user!=null){
                if (user.isActive()) {
                    user.setActive(false);
                } else {
                    user.setActive(true);
                }
            }
            userRepository.save(user);
        }*/
    public List<User> list() {
        return userRepository.findAll();

    }

    public void changeUserRoles(User user, Map<String, String> form) {
        Set<String> roles = Arrays.stream(Role.values())
                .map(Role::name)
                .collect(Collectors.toSet());

        user.getRoles().clear();

        for (String key : form.keySet()) {
            if (roles.contains(key)) {
                user.getRoles().add(Role.valueOf(key));
            }
        }
        userRepository.save(user);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}