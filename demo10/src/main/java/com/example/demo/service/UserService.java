package com.example.demo.service;

import com.example.demo.models.User;
import com.example.demo.models.enums.Role;
import com.example.demo.rep.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;


@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public boolean createUser(User user) {
        String email = user.getName();
        if (userRepository.findByEmail(email) != null) return false;
        user.setActive(true);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.getRoles().add(Role.ROLE_USER);
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
/*public List<User> list(){
        return userRepository.findAll();

}*/
/*    public User getUserByPrincipal(Principal principal) {
        if (principal == null) return new User();
        return userRepository.findByEmail(principal.getName());
    }*/
/*
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
}
*/
}