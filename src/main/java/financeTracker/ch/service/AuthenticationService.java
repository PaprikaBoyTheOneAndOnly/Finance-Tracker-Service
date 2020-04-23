package financeTracker.ch.service;

import financeTracker.ch.model.Credentials;
import financeTracker.ch.model.Token;
import financeTracker.ch.pesrsistence.User;
import financeTracker.ch.pesrsistence.UserRepository;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;

import javax.annotation.PostConstruct;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.stream.Collectors;

@Component
@ApplicationScope
public class AuthenticationService {
    @Value("${security.key}")
    private String securityKey;
    private Key key;

    private final UserRepository userRepository;

    private Map<Token, User> signInUsers;

    @Autowired
    public AuthenticationService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.signInUsers = new HashMap<>();
    }

    @PostConstruct
    private void setUp() {
        this.key = new SecretKeySpec(
                getHash(securityKey).getBytes(StandardCharsets.UTF_8),
                SignatureAlgorithm.HS256.getJcaName()
        );
    }

    public Optional<Token> authenticateUser(Credentials credentials) {
        Optional<User> user = this.userRepository
                .findByCredentials(credentials.getEmail(), this.getHash(credentials.getPassword()));

        if (user.isPresent()) {
            Token token = new Token(this.generateNewJWToken(user.get().getId()));
            this.signInUsers = this.signInUsers.entrySet().stream()
                    .filter(entry -> !(entry.getValue().getId() == user.get().getId()))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            this.signInUsers.put(token, user.get());
            return Optional.of(token);
        }

        return Optional.empty();
    }

    public Optional<User> checkAuthToken(Token token) {
        try {
            this.extractUserId(token.getValue());
        } catch (JwtException e) {
            return Optional.empty();
        }
        return Optional.ofNullable(this.signInUsers.get(token));
    }

    public boolean logoutUser(Token token) {
        if (this.signInUsers.containsKey(token)) {
            this.signInUsers.remove(token);
            return true;
        }
        return false;
    }

    public Map<Token, User> getSignInUsers() {
        return signInUsers;
    }

    public void setSignInUsers(Map<Token, User> signInUsers) {
        this.signInUsers = signInUsers;
    }

    public Integer extractUserId(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("userId", Integer.class);
    }

    private String generateNewJWToken(int userId) {
        Calendar date = Calendar.getInstance();
        long t = date.getTimeInMillis();
        Date expirationDate = new Date(t + (20 * 6000)); // minutes * one-minute-in-millis

        return Jwts.builder()
                .setIssuer("http://localhost")
                .claim("userId", userId)
                .setExpiration(expirationDate)
                .signWith(this.key, SignatureAlgorithm.HS256)
                .compact();
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
}
