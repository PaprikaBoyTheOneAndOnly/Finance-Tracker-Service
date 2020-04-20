package financeTracker.ch.service;

import financeTracker.ch.model.Credentials;
import financeTracker.ch.model.Token;
import financeTracker.ch.pesrsistence.User;
import financeTracker.ch.pesrsistence.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Component
@ApplicationScope
public class AuthenticationService {
    private final UserRepository userRepository;

    private Map<Token, User> signInUsers;

    @Autowired
    public AuthenticationService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.signInUsers = new HashMap<>();
    }

    public Optional<Token> authenticateUser(Credentials credentials) {
        Optional<User> user = this.userRepository
                .findByCredentials(credentials.getEmail(), this.getHash(credentials.getPassword()));

        if (user.isPresent()) {
            Token token = new Token(UUID.randomUUID().toString(), user.get().getId());
            this.signInUsers.put(token, user.get());
            return Optional.of(token);
        }

        return Optional.empty();
    }

    public Optional<User> checkAuthToken(Token token) {
        return Optional.ofNullable(this.signInUsers.get(token));
    }

    public void logoutUser(Token token) {
        this.signInUsers.remove(token);
    }

    private String getHash(String inStr) {
        StringBuilder hexString = new StringBuilder();

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(inStr.getBytes(StandardCharsets.UTF_8));

            for (byte b : encodedHash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return hexString.toString();
    }

    public Map<Token, User> getSignInUsers() {
        return signInUsers;
    }

    public void setSignInUsers(Map<Token, User> signInUsers) {
        this.signInUsers = signInUsers;
    }
}
