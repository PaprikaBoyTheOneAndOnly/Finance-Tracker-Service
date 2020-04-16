package financeTracker.ch.control;

import financeTracker.ch.model.Credentials;
import financeTracker.ch.model.Token;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequestMapping("/login")
public interface LoginController {
    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    ResponseEntity<Token> login(@RequestBody Credentials credentials);

    @DeleteMapping()
    ResponseEntity logout(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String token);
}
