package financeTracker.ch.control;

import financeTracker.ch.model.RESTSpending;
import financeTracker.ch.model.Token;
import financeTracker.ch.service.AuthenticationService;
import financeTracker.ch.service.SpendingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.annotation.RequestScope;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RequestScope
@RestController
public class SpendingControllerV1Impl implements SpendingController {
    private final SpendingService spendingService;
    private final AuthenticationService authenticationService;

    @Autowired
    public SpendingControllerV1Impl(SpendingService spendingService, AuthenticationService authenticationService) {
        this.spendingService = spendingService;
        this.authenticationService = authenticationService;
    }

    @Override
    public ResponseEntity<List<RESTSpending>> loadSpendings(String token) {
        int userId = this.authenticationService.extractUserId(token);
        List<RESTSpending> spendings = this.spendingService.getSpendingByUser(userId);
        return ResponseEntity.ok(spendings);
    }

    @Override
    public ResponseEntity<Integer> deleteSpending(@PathVariable int id) {
        int deleted = this.spendingService.deleteSpending(id);
        return (deleted == 1 ? ResponseEntity.ok() : ResponseEntity.status(404)).build();
    }

    @Override
    public ResponseEntity<RESTSpending> updateSpending(RESTSpending rs, HttpServletRequest request) {
        System.out.println("rs = " + rs);
        String tokenString = request.getHeader(HttpHeaders.AUTHORIZATION);
        Token userToken = new Token(tokenString);
        RESTSpending updated = this.spendingService.updateSpending(rs, userToken);
        return ResponseEntity.ok(updated);
    }

    @Override
    public ResponseEntity<RESTSpending> insertSpending(RESTSpending rs, HttpServletRequest request) {
        System.out.println("rs = " + rs);
        String tokenString = request.getHeader(HttpHeaders.AUTHORIZATION);
        Token userToken = new Token(tokenString);
        RESTSpending insertedSpending = this.spendingService.insertSpending(rs, userToken);
        return ResponseEntity.ok(insertedSpending);
    }
}
