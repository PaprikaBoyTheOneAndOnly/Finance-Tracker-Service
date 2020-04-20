package financeTracker.ch.service;

import financeTracker.ch.pesrsistence.Spending;
import financeTracker.ch.pesrsistence.SpendingRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.Arrays;

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
}


