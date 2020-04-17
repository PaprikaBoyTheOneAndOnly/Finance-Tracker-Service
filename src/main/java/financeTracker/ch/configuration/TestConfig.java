package financeTracker.ch.configuration;

import financeTracker.ch.service.MailReceiverService;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Profile("Test")
@Configuration
public class TestConfig {
    @Bean
    @Primary
    public MailReceiverService mailService() {
        return Mockito.mock(MailReceiverService.class);
    }
}
