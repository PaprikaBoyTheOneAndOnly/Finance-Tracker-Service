package financeTracker.ch.service;

import financeTracker.ch.model.RESTSpending;
import financeTracker.ch.pesrsistence.Spending;
import financeTracker.ch.pesrsistence.SpendingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.ApplicationScope;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SpendingService {
    private final SpendingRepository spendingRepository;

    @Autowired
    public SpendingService(SpendingRepository spendingRepository) {
        this.spendingRepository = spendingRepository;
    }

    public List<RESTSpending> getSpendingByUser(int userId) {
        return this.spendingRepository.findByUserId(userId).stream()
                .map(RESTSpending::new)
                .collect(Collectors.toList());
    }

    public int deleteSpending(int id) {
        return this.spendingRepository.deleteSpendingById(id);
    }

    public RESTSpending updateSpending(RESTSpending rs) {
        Spending spending = new Spending(rs.getId(), rs.getAmount(), rs.getDescription(), rs.parsedDate(), rs.getType());
        return new RESTSpending(this.spendingRepository.save(spending));
    }

    public RESTSpending insertSpending(RESTSpending rs) {
        Spending newSpending = new Spending(rs.getId(), rs.getAmount(), rs.getDescription(), rs.parsedDate(), rs.getType());
        Spending s = this.spendingRepository.save(newSpending);
        return new RESTSpending(s);
    }
}
