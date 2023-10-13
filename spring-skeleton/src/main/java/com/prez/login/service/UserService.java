package com.prez.login.service;

import com.prez.login.model.LoginDTO;
import com.prez.login.model.User;
import com.prez.login.model.UserRegistrationDTO;
import com.prez.login.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;


    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> findById(long id) {
        return userRepository.findById(id);
    }
    // Now, you can use userRepository to interact with the database

    public void register(UserRegistrationDTO userDTO) throws Exception {
        if (userRepository.existsByUsername(userDTO.getUsername())) {
            throw new Exception("Username already exists");
        }

        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new Exception("Email already exists");
        }

        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPasswordHash(PasswordService.hashPassword(userDTO.getPassword()));
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());

        userRepository.save(user);
    }

    public String login(LoginDTO loginDTO) {
        Optional<User> userOptional = userRepository.findByUsername(loginDTO.getUsername());

        if (userOptional.isPresent() && PasswordService.checkPassword(loginDTO.getPassword(), userOptional.get().getPasswordHash())) {
            // If the password is valid, generate a JWT for the user.
            // replace with a real token \/
            return "dummy-jwt-token";
        }

        return null;
    }
}