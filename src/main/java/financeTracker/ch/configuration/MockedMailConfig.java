package financeTracker.ch.configuration;

import financeTracker.ch.model.MailType;
import financeTracker.ch.service.MailReceiverService;
import financeTracker.ch.service.MailSenderService;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Profile("MailMocked")
@Configuration
public class MockedMailConfig {
    private final Logger logger;

    @Autowired
    public MockedMailConfig(Logger logger) {
        this.logger = logger;
    }

    @Bean
    @Primary
    public MailReceiverService mailReceiverService() {
        this.logger.info("Started with MailReceiver Mock");
        return Mockito.mock(MailReceiverService.class);
    }

    @Bean
    @Primary
    public MailSenderService mailSenderService() {
        return new MockedMailSenderService();
    }

    private class MockedMailSenderService extends MailSenderService  {
        private Logger logger;

        public MockedMailSenderService() {
            super(null);
            this.logger = LoggerFactory.getLogger(MockedMailSenderService.class);
        }

        @Override
        public void sendEmail(String to, String msg, MailType type) {
            this.logger.info("[MockedMailSenderService] --- Sending Mail ---" +
                    "\nTo: " + to +
                    "\nMessage: " + msg +
                    "\nType: " + type);
        }
    }
}
