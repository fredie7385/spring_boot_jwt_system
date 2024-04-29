package spring_boot_jwt_system.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;
import spring_boot_jwt_system.model.Token;
import spring_boot_jwt_system.repository.TokenRepository;

@Component
public class CustomLogoutHandler implements LogoutHandler{

    private final TokenRepository tokenRepository;

    public CustomLogoutHandler(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Override
    public void logout(HttpServletRequest request,
                       HttpServletResponse response,
                       Authentication authentication) {
        String autHeader =request.getHeader("Authorization");
        if (autHeader ==null || !autHeader.startsWith("Bearer")){
            return;
        }
        String token =autHeader.substring(7);

        // get stored token from database
        Token storedToken = tokenRepository.findByToken(token).orElse(null);

        // invalidate the token (i.e. make logout true)
        if (token != null){
            storedToken.setLoggedOut(true);
            tokenRepository.save(storedToken);
        }
    }
}
