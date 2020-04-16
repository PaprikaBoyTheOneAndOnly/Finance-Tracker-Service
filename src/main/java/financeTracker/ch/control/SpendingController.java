package financeTracker.ch.control;

import financeTracker.ch.model.RESTSpending;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequestMapping("/spendings")
public interface SpendingController {
    @GetMapping(produces = APPLICATION_JSON_VALUE)
    ResponseEntity<List<RESTSpending>> loadSpendings(@RequestParam int userId);
}
