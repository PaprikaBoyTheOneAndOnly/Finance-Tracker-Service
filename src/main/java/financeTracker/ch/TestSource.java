package financeTracker.ch;

import financeTracker.ch.pesrsistence.SpendingRepository;
import financeTracker.ch.pesrsistence.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestSource {
    @Autowired
    private SpendingRepository spendingRepository;
    @Autowired
    private UserRepository userRepository;
    @GetMapping("/test")
    public void test() {

    }
}
