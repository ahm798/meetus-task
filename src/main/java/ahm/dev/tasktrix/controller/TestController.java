package ahm.dev.tasktrix.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping("/hello")
    public ResponseEntity<String> hello() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth != null ? auth.getName() : "Anonymous";
        return ResponseEntity.ok("Hello " + username + "! Your authentication is working.");
    }

    @GetMapping("/secure")
    public ResponseEntity<String> secure() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok("This is a secure endpoint. User: " + 
                                (auth != null ? auth.getName() : "Unknown") + 
                                ", Authorities: " + 
                                (auth != null ? auth.getAuthorities() : "None"));
    }
}
