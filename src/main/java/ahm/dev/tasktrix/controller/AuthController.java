package ahm.dev.tasktrix.controller;

import ahm.dev.tasktrix.domain.User;
import ahm.dev.tasktrix.dto.UserForRegister;
import ahm.dev.tasktrix.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserForRegister userForRegister) {
        userService.createUser(userForRegister);
        return ResponseEntity.ok("User registered successfully");
    }
}
