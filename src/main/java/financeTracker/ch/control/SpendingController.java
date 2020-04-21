package financeTracker.ch.control;

import financeTracker.ch.model.RESTSpending;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequestMapping("/spendings")
public interface SpendingController {
    @GetMapping(produces = APPLICATION_JSON_VALUE)
    ResponseEntity<List<RESTSpending>> loadSpendings(@RequestParam int userId);

    @DeleteMapping(path = "/{id}", produces = APPLICATION_JSON_VALUE)
    ResponseEntity<Integer> deleteSpending(@PathVariable int id);

    @PutMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    ResponseEntity<RESTSpending> updateSpending(@RequestBody RESTSpending spending);

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    ResponseEntity<RESTSpending> insertSpending(@RequestBody RESTSpending newSpending);
}
