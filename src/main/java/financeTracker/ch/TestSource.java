package financeTracker.ch;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestSource {
    @GetMapping("/test")
    public void test() {
    }
}
