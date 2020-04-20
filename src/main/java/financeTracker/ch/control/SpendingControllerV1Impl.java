package financeTracker.ch.control;

import financeTracker.ch.model.RESTSpending;
import financeTracker.ch.pesrsistence.Spending;
import financeTracker.ch.service.SpendingService;
import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.annotation.RequestScope;

import javax.validation.Valid;
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

    @Override
    public ResponseEntity<Integer> deleteSpending(@PathVariable int id) {
        int deleted = this.spendingService.deleteSpending(id);
        System.out.println("deleted = " + deleted);
        return (deleted == 1? ResponseEntity.ok() : ResponseEntity.status(404)).build();
    }

    @Override
    public ResponseEntity<RESTSpending> updateSpending(RESTSpending rs) {
        RESTSpending updated = this.spendingService.updateSpending(rs);
        return ResponseEntity.ok(updated);
    }

    @Override
    public ResponseEntity<RESTSpending> insertSpending(RESTSpending rs) {
        RESTSpending insertedSpending = this.spendingService.insertSpending(rs);
        return ResponseEntity.ok(insertedSpending);
    }
}
