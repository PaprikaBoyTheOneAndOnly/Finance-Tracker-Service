package financeTracker.ch.security;

import financeTracker.ch.model.Token;
import financeTracker.ch.pesrsistence.User;
import financeTracker.ch.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Component
public class SecurityInterceptor implements HandlerInterceptor {
    private final AuthenticationService authenticationService;

    @Autowired
    public SecurityInterceptor(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (token == null) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        }

        Optional<User> user = authenticationService.checkAuthToken(new Token(token));
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        return user.isPresent();
    }
}
