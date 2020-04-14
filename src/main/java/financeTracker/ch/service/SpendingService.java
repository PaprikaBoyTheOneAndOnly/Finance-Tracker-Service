package financeTracker.ch.service;

import financeTracker.ch.model.Spending;
import financeTracker.ch.model.UserNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.ApplicationScope;

import java.util.List;

@Service
@ApplicationScope
public class SpendingService {
    public SpendingService() {
    }

    public List<Spending> getSpendingByUser(int userId) throws UserNotFoundException {
        return Mock.mockUsers.stream()
                .filter(user -> user.getId() == userId)
                .findFirst()
                .orElseThrow(() -> new UserNotFoundException(userId))
                .getSpendings();
    }
}
