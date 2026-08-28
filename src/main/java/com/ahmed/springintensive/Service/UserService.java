package com.ahmed.springintensive.Service;

import com.ahmed.springintensive.DTO.RegisterRequest;
import com.ahmed.springintensive.Entity.User;
import com.ahmed.springintensive.Repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserService {

    private final UserRepository userRepository;
    private  final PasswordEncoder passwordEncoder;


    public  UserService(UserRepository userRepository,PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User register(RegisterRequest request){
        if(userRepository.findByUsername(request.getUsername()).isPresent()){
            throw  new RuntimeException("Username already exists");
        }

        String encodePassword = passwordEncoder.encode(request.getPassword());

        User user = new User(
                request.getUsername(),
                encodePassword,
                "USER"
        );

        return userRepository.save(user);
    }
}
