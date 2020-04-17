package financeTracker.ch;


import financeTracker.ch.service.MailReceiverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class TaskStarter implements CommandLineRunner {
    private final MailReceiverService mailReceiverService;

    @Autowired
    public TaskStarter(MailReceiverService mailReceiverService) {
        this.mailReceiverService = mailReceiverService;
    }

    @Override
    public void run(String... args){
        this.mailReceiverService.start();
    }
}
