package financeTracker.ch.service;

import financeTracker.ch.model.RESTSpending;
import financeTracker.ch.model.UserNotFoundException;
import financeTracker.ch.pesrsistence.SpendingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.ApplicationScope;

import java.util.List;
import java.util.stream.Collectors;

@Service
@ApplicationScope
public class SpendingService {
    private final SpendingRepository spendingRepository;

    @Autowired
    public SpendingService(SpendingRepository spendingRepository) {
        this.spendingRepository = spendingRepository;
    }

    public List<RESTSpending> getSpendingByUser(int userId) throws UserNotFoundException {
        return this.spendingRepository.findByUserId(userId).stream()
                .map(RESTSpending::new)
                .collect(Collectors.toList());
    }
}
