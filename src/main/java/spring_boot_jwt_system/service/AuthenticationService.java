package spring_boot_jwt_system.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import spring_boot_jwt_system.model.AuthenticationResponse;
import spring_boot_jwt_system.model.Token;
import spring_boot_jwt_system.model.UserModel;
import spring_boot_jwt_system.repository.TokenRepository;
import spring_boot_jwt_system.repository.UserRepository;
import spring_boot_jwt_system.service.impl.JwtService;

import java.util.List;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final TokenRepository tokenRepository;

    public AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager, TokenRepository tokenRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.tokenRepository = tokenRepository;
    }

    public AuthenticationResponse register(UserModel request) {
        UserModel userModel = new UserModel();
        userModel.setFirstName(request.getFirstName());
        userModel.setLastName(request.getLastName());
        userModel.setUsername(request.getUsername());
        userModel.setPassword(passwordEncoder.encode(request.getPassword()));
        userModel.setRoleModel(request.getRoleModel());
        userModel = userRepository.save(userModel);

        String jwt = jwtService.generateToken(userModel);
        // save the generated token
        saveUserToken(jwt, userModel);
        return new AuthenticationResponse(jwt);
    }

    public AuthenticationResponse authenticate(UserModel request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        UserModel userModel = userRepository.findByUsername(request.getUsername()).orElseThrow();
        String token = jwtService.generateToken(userModel);

        revokeAllTokenByUser(userModel);
        saveUserToken(token, userModel);
        return new AuthenticationResponse(token);
    }

    private void revokeAllTokenByUser(UserModel userModel) {
        List<Token> validTokenListByUserModel = tokenRepository.findAllTokenByUser(userModel.getId());
        if (!validTokenListByUserModel.isEmpty()) {
            validTokenListByUserModel.forEach(t -> {
                t.setLoggedOut(true);
            });
        }
        tokenRepository.saveAll(validTokenListByUserModel);
    }

    private void saveUserToken(String jwt, UserModel userModel) {
        Token token = new Token();
        token.setToken(jwt);
        token.setLoggedOut(false);
        token.setUserModel(userModel);
        tokenRepository.save(token);
    }
}
