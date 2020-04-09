package financeTracker.ch.control;

import financeTracker.ch.model.Spending;
import financeTracker.ch.model.SpendingType;
import financeTracker.ch.model.UserNotFoundException;
import financeTracker.ch.service.SpendingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.annotation.RequestScope;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RequestScope
@RestController
@RequestMapping("/spendings")
public class SpendingController {
    private SpendingService spendingService;

    @Autowired
    public SpendingController(SpendingService spendingService) {
        this.spendingService = spendingService;
    }

    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Spending>> loadSpendings(@RequestParam int userId) {
        List<Spending> spendings = spendingService.getSpendingByUser(userId);
        return ResponseEntity.ok(spendings);
    }
}
