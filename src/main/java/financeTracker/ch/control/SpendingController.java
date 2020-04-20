package financeTracker.ch.control;

import financeTracker.ch.model.RESTSpending;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequestMapping("/spendings")
public interface SpendingController {
    @GetMapping(produces = APPLICATION_JSON_VALUE)
    ResponseEntity<List<RESTSpending>> loadSpendings(@RequestParam int userId);

    @DeleteMapping(path = "/{id}")
    ResponseEntity<Integer> deleteSpending(@PathVariable int id);

    @PutMapping(path = "/")
    ResponseEntity<RESTSpending> updateSpending(@Valid @RequestBody RESTSpending spending);

    @PostMapping(path = "/")
    ResponseEntity<RESTSpending> insertSpending(@RequestBody RESTSpending newSpending);
}
