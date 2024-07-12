package com.shep.Controllers;

import com.shep.Entities.User;
import com.shep.Entities.UserSecret;
import com.shep.Models.ApiResponse;
import com.shep.Models.JwtAuthenticationResponse;
import com.shep.Security.JwtTokenProvider;
import com.shep.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        return ResponseEntity.ok(userService.saveUser(user));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody JwtAuthenticationRequest authenticationRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userService.findByUsername(userDetails.getUsername());

        // генерируем новый секретный ключ для пользователя
        String secretKey = UUID.randomUUID().toString();
        UserSecret userSecret = new UserSecret();
        userSecret.setSecretKey(secretKey);
        userSecretService.save(userSecret);

        // обновляем ссылку на секретный ключ пользователя
        user.setUserSecret(userSecret);
        userService.save(user);

        // генерируем JWT-токен с использованием нового секретного ключа
        String jwtToken = jwtTokenProvider.generateToken(authentication, secretKey);

        return ResponseEntity.ok(new JwtAuthenticationResponse(jwtToken));
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add-user") // Исправленная аннотация
    public ResponseEntity<?> addUser(@RequestBody User user) {
        return ResponseEntity.ok(userService.saveUser(user));
    }

}
