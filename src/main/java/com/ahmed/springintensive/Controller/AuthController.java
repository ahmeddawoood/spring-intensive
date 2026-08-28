package com.ahmed.springintensive.Controller;

import com.ahmed.springintensive.DTO.RegisterRequest;
import com.ahmed.springintensive.DTO.UserResponse;
import com.ahmed.springintensive.Entity.User;
import com.ahmed.springintensive.Service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(
            @RequestBody RegisterRequest request
    ) {
        User user = userService.register(request);

        UserResponse response = new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getRole()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
}