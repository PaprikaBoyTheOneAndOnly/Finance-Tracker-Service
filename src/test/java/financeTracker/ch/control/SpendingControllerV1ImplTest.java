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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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
                new RESTSpending(new Spending(1, 12.50, "its a description", null, SpendingType.SINGLE)));
        String expectedSpendingsString = this.mapper.writeValueAsString(mockSpendings);

        when(this.mockSpendingService.getSpendingByUser(anyInt())).thenReturn(mockSpendings);

        this.mockMvc.perform(get("/spendings?userId=1")
                .header(HttpHeaders.AUTHORIZATION, "Bearer iAmAToken"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo(expectedSpendingsString)));
    }

    @Test
    public void testGetSpendingsByUserId_noUserIdGiven() throws Exception {
        this.mockMvc.perform(get("/spendings")
                .header(HttpHeaders.AUTHORIZATION, "Bearer iAmAToken"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(status().reason(equalTo("Required int parameter 'userId' is not present")));
    }

    @Test
    public void testGetSpendingsByUserId_notLoggedIn() throws Exception {
        this.mockMvc.perform(get("/spendings"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }
}
