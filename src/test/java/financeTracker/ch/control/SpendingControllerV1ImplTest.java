package financeTracker.ch.control;

import com.fasterxml.jackson.databind.ObjectMapper;
import financeTracker.ch.model.RESTSpending;
import financeTracker.ch.model.SpendingType;
import financeTracker.ch.model.Token;
import financeTracker.ch.pesrsistence.Spending;
import financeTracker.ch.pesrsistence.User;
import financeTracker.ch.service.AuthenticationService;
import financeTracker.ch.service.SpendingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("MailMocked")
@WebMvcTest(SpendingControllerV1Impl.class)
@ExtendWith(SpringExtension.class)
public class SpendingControllerV1ImplTest {
    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private SpendingService mockSpendingService;
    @MockBean
    private AuthenticationService authenticationService;

    @BeforeEach
    public void setUp() {
        when(authenticationService.checkAuthToken(any(Token.class)))
                .thenReturn(Optional.of(new User()));
    }

    @Test
    public void testGetSpendingsByUserId() throws Exception {
        List<RESTSpending> mockSpendings = Arrays.asList(
                new RESTSpending(new Spending(1, 12.50, "its a description", LocalDate.now(), SpendingType.SINGLE)));
        String expectedSpendingsString = this.mapper.writeValueAsString(mockSpendings);

        when(this.mockSpendingService.getSpendingByUser(anyInt())).thenReturn(mockSpendings);

        this.mockMvc.perform(get("/spendings")
                .header(HttpHeaders.AUTHORIZATION, "Bearer iAmAToken"))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo(expectedSpendingsString)));
    }

    @Test
    public void testGetSpendingsByUserId_noUserIdGivenInHeader() throws Exception {
        when(authenticationService.checkAuthToken(any(Token.class))).thenReturn(Optional.empty());

        this.mockMvc.perform(get("/spendings")
                .header(HttpHeaders.AUTHORIZATION, "Bearer iAmAWrongToken"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testGetSpendingsByUserId_notLoggedIn() throws Exception {
        this.mockMvc.perform(get("/spendings"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testDeleteSpending() throws Exception {
        when(mockSpendingService.deleteSpending(anyInt())).thenReturn(1);
        this.mockMvc.perform(delete("/spendings/1")
                .header(HttpHeaders.AUTHORIZATION, "Bearer iAmAToken"))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteSpending_invalidSpendingId() throws Exception {
        when(mockSpendingService.deleteSpending(anyInt())).thenReturn(0);
        this.mockMvc.perform(delete("/spendings/-1")
                .header(HttpHeaders.AUTHORIZATION, "Bearer iAmAToken"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteSpending_notLoggedIn() throws Exception {
        // No Auth-Header given
        this.mockMvc.perform(delete("/spendings/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testInsertSpending() throws Exception {
        RESTSpending mockSpending = new RESTSpending(new Spending(0, 12.50, "its a description", LocalDate.now(), SpendingType.SINGLE));
        String inputSpendingString = this.mapper.writeValueAsString(mockSpending);
        mockSpending.setId(10);
        String expectedSpendingString = this.mapper.writeValueAsString(mockSpending);

        when(this.mockSpendingService.insertSpending(any(RESTSpending.class))).thenReturn(mockSpending);

        this.mockMvc.perform(post("/spendings/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(inputSpendingString)
                .header(HttpHeaders.AUTHORIZATION, "Bearer iAmAToken"))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo(expectedSpendingString)));
    }

    @Test
    public void testInsertSpending_notLoggedIn() throws Exception {
        RESTSpending mockSpending = new RESTSpending(new Spending(0, 12.50, "its a description", LocalDate.now(), SpendingType.SINGLE));
        String inputSpendingString = this.mapper.writeValueAsString(mockSpending);
        when(this.mockSpendingService.insertSpending(any(RESTSpending.class))).thenReturn(mockSpending);

        // No Auth-Header given
        this.mockMvc.perform(post("/spendings/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(inputSpendingString))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testUpdateSpending() throws Exception {
        RESTSpending mockSpending = new RESTSpending(new Spending(1, 12.50, "better updated spending", LocalDate.now(), SpendingType.SINGLE));
        String inputSpendingString = this.mapper.writeValueAsString(mockSpending);

        when(this.mockSpendingService.updateSpending(any(RESTSpending.class))).thenReturn(mockSpending);

        this.mockMvc.perform(put("/spendings/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(inputSpendingString)
                .header(HttpHeaders.AUTHORIZATION, "Bearer iAmAToken"))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo(inputSpendingString)));
    }

}
