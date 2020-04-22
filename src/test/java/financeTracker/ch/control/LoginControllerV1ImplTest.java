package financeTracker.ch.control;

import com.fasterxml.jackson.databind.ObjectMapper;
import financeTracker.ch.model.Credentials;
import financeTracker.ch.model.Token;
import financeTracker.ch.service.AuthenticationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;

import java.util.Optional;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("MailMocked")
@WebMvcTest(LoginControllerV1Impl.class)
public class LoginControllerV1ImplTest {
    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AuthenticationService mockAuthenticationService;

    @Test
    public void testPostLogin() throws Exception {
        Token mockToken = new Token("iAmAToken");
        String expectedTokenString = this.mapper.writeValueAsString(mockToken);
        when(mockAuthenticationService.authenticateUser(any(Credentials.class))).
                thenReturn(Optional.of(mockToken));
        RequestBuilder post =
                post("/login")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapper.writeValueAsString(new Credentials("Peter", "12345")));

        this.mockMvc.perform(post)
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo(expectedTokenString)));
    }

    @Test
    public void testPostLogin_unauthorized() throws Exception {
        RequestBuilder post =
                post("/login")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapper.writeValueAsString(new Credentials("Peter", "12345")));

        this.mockMvc.perform(post)
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testDeleteLogin() throws Exception {
        when(mockAuthenticationService.logoutUser(any(Token.class)))
                .thenReturn(true);

        this.mockMvc.perform(delete("/login")
                .header(HttpHeaders.AUTHORIZATION, "Bearer iAmAToken"))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteLogin_userWasNotLoggedIn() throws Exception {
        when(mockAuthenticationService.logoutUser(any(Token.class)))
                .thenReturn(false);

        this.mockMvc.perform(delete("/login")
                .header(HttpHeaders.AUTHORIZATION, "Bearer iAmAToken"))
                .andExpect(status().isNotFound());
    }
}
