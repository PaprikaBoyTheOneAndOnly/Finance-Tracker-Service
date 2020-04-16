package financeTracker.ch.service;

import financeTracker.ch.model.Credentials;
import financeTracker.ch.model.Token;
import financeTracker.ch.pesrsistence.User;
import financeTracker.ch.pesrsistence.UserRepository;
import org.hamcrest.collection.IsMapContaining;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
public class AuthenticationServiceTest {
    private final String basicPass123456 = "8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92";

    @Autowired
    private AuthenticationService authenticationService;
    @MockBean
    private UserRepository mockUserRepository;

    @BeforeEach
    public void setUp() {
        this.authenticationService.setSignInUsers(new HashMap<>());
    }

    @Test
    public void testAuthenticateUser() {
        when(this.mockUserRepository.findByCredentials(anyString(), anyString()))
                .thenReturn(Optional.of(new User(1, "Peter@gmail.com", basicPass123456, new ArrayList<>())));

        Optional<Token> token = this.authenticationService.authenticateUser(new Credentials("Peter@gmail.com", "123456"));

        assertThat(token.isPresent(), is(true));
        Map<Token, User> users = this.authenticationService.getSignInUsers();
        assertThat(users.size(), is(1));
        assertThat(users.get(token.get()).getEmail(), is("Peter@gmail.com"));
    }

    @Test
    public void testAuthenticateUser_credentialsWrong() {
        when(this.mockUserRepository.findByCredentials(anyString(), anyString()))
                .thenReturn(Optional.empty());

        Optional<Token> token = this.authenticationService.authenticateUser(new Credentials("Peter@gmail.com", "123456"));

        assertThat(token.isPresent(), is(false));
        Map<Token, User> users = this.authenticationService.getSignInUsers();
        assertThat(users.size(), is(0));
    }

    @Test
    public void testCheckAuthToken() {
        when(this.mockUserRepository.findByCredentials(anyString(), anyString()))
                .thenReturn(Optional.of(new User(1, "Peter@gmail.com", basicPass123456, new ArrayList<>())));
        Optional<Token> token = this.authenticationService.authenticateUser(new Credentials("Peter@gmail.com", "123456"));

        if(token.isPresent()) {
            Optional<User> user = this.authenticationService.checkAuthToken(token.get());
            assertThat(user.isPresent(), is(true));
            assertThat(user.get().getEmail(), is("Peter@gmail.com"));
        } else {
            fail("Token is empty!");
        }
    }

    @Test
    public void testCheckAuthToken_noTokenAvailable() {
        Optional<User> user = this.authenticationService.checkAuthToken(new Token("IAmAToken"));
        assertThat(user.isPresent(), is(false));
    }

    @Test
    public void testLogoutUser() {
        User mockedUser = new User(1, "Peter@gmail.com", basicPass123456, new ArrayList<>());
        when(this.mockUserRepository.findByCredentials(anyString(), anyString()))
                .thenReturn(Optional.of(mockedUser));
        Optional<Token> token = this.authenticationService.authenticateUser(new Credentials("Peter@gmail.com", "123456"));

        if(token.isPresent()) {
            this.authenticationService.logoutUser(new Token("iAmNotKnown"));
            this.authenticationService.logoutUser(token.get());
            Map<Token, User> users = this.authenticationService.getSignInUsers();
            assertThat(users, not(IsMapContaining.hasEntry(token.get(), mockedUser)));
        } else {
            fail("Token is empty!");
        }
    }
}
