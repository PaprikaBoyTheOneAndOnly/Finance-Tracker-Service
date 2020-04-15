package financeTracker.ch.control;

import financeTracker.ch.model.RESTSpending;
import financeTracker.ch.service.SpendingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.annotation.RequestScope;

import java.util.List;

@RequestScope
@RestController
@RequestMapping("/spendings")
public class SpendingController {
    private final SpendingService spendingService;

    @Autowired
    public SpendingController(SpendingService spendingService) {
        this.spendingService = spendingService;
    }

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<RESTSpending>> loadSpendings(@RequestParam int userId) {
        List<RESTSpending> spendings = this.spendingService.getSpendingByUser(userId);
        return ResponseEntity.ok(spendings);
    }
}
