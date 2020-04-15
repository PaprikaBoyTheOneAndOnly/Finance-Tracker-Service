package financeTracker.ch.control;

import com.fasterxml.jackson.databind.ObjectMapper;
import financeTracker.ch.model.RESTSpending;
import financeTracker.ch.model.SpendingType;
import financeTracker.ch.pesrsistence.Spending;
import financeTracker.ch.service.SpendingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(SpendingController.class)
public class SpendingControllerTest {
    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private SpendingService spendingService;

    @Test
    public void testDefaultPath() throws Exception {
        List<RESTSpending> mockSpendings = Arrays.asList(
                new RESTSpending(new Spending(1, 12.50, "its a description", null, SpendingType.SINGLE)));
        String expectedSpendingsString = this.mapper.writeValueAsString(mockSpendings);

        when(this.spendingService.getSpendingByUser(anyInt())).thenReturn(mockSpendings);

        this.mockMvc.perform(get("/spendings?userId=1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo(expectedSpendingsString)));
    }

    @Test
    public void testDefaultPath_noUserIdGiven() throws Exception {
        this.mockMvc.perform(get("/spendings"))
                .andDo(print())
                .andExpect(status().is(400))
                .andExpect(status().reason(equalTo("Required int parameter 'userId' is not present")));
    }
}
