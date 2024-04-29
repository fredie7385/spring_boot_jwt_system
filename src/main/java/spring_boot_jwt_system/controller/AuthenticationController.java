package spring_boot_jwt_system.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import spring_boot_jwt_system.model.AuthenticationResponse;
import spring_boot_jwt_system.model.UserModel;
import spring_boot_jwt_system.service.AuthenticationService;


@RestController
public class AuthenticationController {

    private final AuthenticationService authService;

    public AuthenticationController(AuthenticationService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody UserModel request){
        return ResponseEntity.ok(authService.register(request));
    }
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody UserModel request){
        return ResponseEntity.ok(authService.authenticate(request));
    }
  
}
