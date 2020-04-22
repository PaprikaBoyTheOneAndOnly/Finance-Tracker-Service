package financeTracker.ch.service;

import financeTracker.ch.model.RESTSpending;
import financeTracker.ch.model.SpendingType;
import financeTracker.ch.model.Token;
import financeTracker.ch.pesrsistence.Spending;
import financeTracker.ch.pesrsistence.SpendingRepository;
import financeTracker.ch.pesrsistence.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;

@ActiveProfiles("MailMocked")
@SpringBootTest
public class SpendingServiceTest {
    @Autowired
    private SpendingService spendingService;
    @MockBean
    private SpendingRepository mockSpendingRepository;
    @MockBean
    private AuthenticationService authService;

    @Test
    public void testGetSpendingsByUser() {
        when(this.mockSpendingRepository.findByUserId(anyInt()))
                .thenReturn(Arrays.asList(new Spending(), new Spending(), new Spending()));

        assertThat(this.spendingService.getSpendingByUser(1).size(), is(3));
        verify(this.mockSpendingRepository, times(1)).findByUserId(anyInt());
    }

    @Test
    public void testGetSpendingsByUser_noSuchUser() {
        when(this.mockSpendingRepository.findByUserId(anyInt()))
                .thenReturn(new ArrayList<>());

        assertThat(this.spendingService.getSpendingByUser(0).isEmpty(), is(true));
        verify(this.mockSpendingRepository, times(1)).findByUserId(anyInt());
    }

    @Test
    public void testDeleteSpending() {
        when(mockSpendingRepository.deleteSpendingById(anyInt())).thenReturn(1);
        assertThat(this.spendingService.deleteSpending(1), is(1));
    }

    @Test
    public void testDeleteSpending_invalidId() {
        when(mockSpendingRepository.deleteSpendingById(anyInt())).thenReturn(0);
        assertThat(this.spendingService.deleteSpending(1), is(0));
    }

//    @Test
//    public void testInsertSpending() {
//        Spending mockSpending = new Spending(10.0, "Some Description", LocalDate.now(), SpendingType.SINGLE);
//
//        RESTSpending mockInput = new RESTSpending(mockSpending);
//        User mockUser = new User();
//        when (this.authService.checkAuthToken(any(Token.class))).thenReturn(Optional.of(mockUser));
//        mockSpending.setCreator(mockUser);
//
//        assertThat(this.spendingService.insertSpending(mockInput, new Token("Bearer iAmAToken")), is());
//    }
}


