package financeTracker.ch.control;

import financeTracker.ch.model.Credentials;
import financeTracker.ch.model.Token;
import financeTracker.ch.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.annotation.RequestScope;

import java.util.Optional;

@RequestScope
@RestController
public class LoginControllerV1Impl implements LoginController {
    private final AuthenticationService authenticationService;

    @Autowired
    public LoginControllerV1Impl(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Override
    public ResponseEntity<Token> login(Credentials credentials) {
        Optional<Token> token = this.authenticationService.authenticateUser(credentials);
        return token.isPresent()?
                ResponseEntity.ok(token.get()):
                ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @Override
    public ResponseEntity logout(String token) {
        if(this.authenticationService.logoutUser(new Token(token))) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
