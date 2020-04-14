package financeTracker.ch.service;

import financeTracker.ch.model.Spending;
import financeTracker.ch.model.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
public class SpendingServiceTest {
    private SpendingService testee;

    @BeforeEach
    public void setUp() {
        testee = new SpendingService();
    }

    @Test
    public void testGetSpendingsByUser() {
        // mock here userRepository bzw spendingRepository
        List<Spending> spendings = testee.getSpendingByUser(0);
        assertEquals(3, spendings.size());
    }

    @Test
    public void testGetSpendingsByUser_userNotFound() {
        Exception exception = assertThrows(UserNotFoundException.class, () -> {
            testee.getSpendingByUser(5);
        });

        assertEquals("User with id = 5 could not be found!", exception.getMessage());
    }
}
