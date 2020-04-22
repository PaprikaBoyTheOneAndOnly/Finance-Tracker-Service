package financeTracker.ch.service;

import financeTracker.ch.model.RESTSpending;
import financeTracker.ch.model.Token;
import financeTracker.ch.pesrsistence.Spending;
import financeTracker.ch.pesrsistence.SpendingRepository;
import financeTracker.ch.pesrsistence.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SpendingService {
    private final SpendingRepository spendingRepository;
    private final AuthenticationService authService;

    @Autowired
    public SpendingService(SpendingRepository spendingRepository, AuthenticationService authService) {
        this.spendingRepository = spendingRepository;
        this.authService = authService;
    }

    public List<RESTSpending> getSpendingByUser(int userId) {
        // Load userId from Token
        return this.spendingRepository.findByUserId(userId).stream()
                .map(RESTSpending::new)
                .collect(Collectors.toList());
    }

    public int deleteSpending(int id) {
        return this.spendingRepository.deleteSpendingById(id);
    }

    public RESTSpending updateSpending(RESTSpending rs, Token token) {
        Optional<User> creator = this.authService.checkAuthToken(token);
        Spending spending = new Spending(rs.getId(), rs.getAmount(), rs.getDescription(), rs.parsedDate(), rs.getType());
        spending.setCreator(creator.get());
        return new RESTSpending(this.spendingRepository.save(spending));
    }

    public RESTSpending insertSpending(RESTSpending rs, Token token) {
        Optional<User> creator = this.authService.checkAuthToken(token);
        Spending newSpending = new Spending(rs.getId(), rs.getAmount(), rs.getDescription(), rs.parsedDate(), rs.getType());
        newSpending.setCreator(creator.get());
        Spending s = this.spendingRepository.save(newSpending);
        return new RESTSpending(s);
    }
}
