package controller;

import dto.LoginRequest;
import dto.RegisterRequest;
import lombok.RequiredArgsConstructor;
import model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import security.JWTUtil;
import service.UserService;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final JWTUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest req) {
        User user = User.builder()
                .name(req.getName())
                .email(req.getEmail())
                .password(req.getPassword())
                .role(req.getRole())
                .build();
        return ResponseEntity.ok(userService.save(user));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        return userService.findByEmail(req.getEmail())
                .filter(u -> u.getPassword().equals(req.getPassword()))
                .map(u -> ResponseEntity.ok(jwtUtil.generateToken(u)))
                .orElse(ResponseEntity.status(401).body("Credenciais inválidas"));
    }
}