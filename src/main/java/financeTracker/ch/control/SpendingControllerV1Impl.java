package financeTracker.ch.control;

import financeTracker.ch.model.RESTSpending;
import financeTracker.ch.service.SpendingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.annotation.RequestScope;

import java.util.List;

@RequestScope
@RestController
public class SpendingControllerV1Impl implements SpendingController {
    private final SpendingService spendingService;

    @Autowired
    public SpendingControllerV1Impl(SpendingService spendingService) {
        this.spendingService = spendingService;
    }

    @Override
    public ResponseEntity<List<RESTSpending>> loadSpendings(int userId) {
        List<RESTSpending> spendings = this.spendingService.getSpendingByUser(userId);
        return ResponseEntity.ok(spendings);
    }
}
