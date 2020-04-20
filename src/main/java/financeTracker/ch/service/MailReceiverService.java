package financeTracker.ch.service;

import com.sun.mail.imap.IMAPFolder;
import financeTracker.ch.mail.MessageHandler;
import financeTracker.ch.pesrsistence.SpendingRepository;
import financeTracker.ch.pesrsistence.UserRepository;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import javax.mail.*;
import java.util.Properties;

@Service
public class MailReceiverService {
    @Value( "${mail.password}" )
    private String password;

    private final Logger logger;
    private final UserRepository userRepository;
    private final SpendingRepository spendingRepository;
    private final MailSenderService mailSenderService;

    private Store store;
    private IdleThread idleThread;

    @Autowired
    public MailReceiverService(Logger logger,
                               UserRepository userRepository,
                               SpendingRepository spendingRepository,
                               MailSenderService mailSenderService) {
        this.logger = logger;
        this.userRepository = userRepository;
        this.spendingRepository = spendingRepository;
        this.mailSenderService = mailSenderService;

        Properties props = System.getProperties();
        props.put("mail.store.protocol", "imaps");
        props.put("mail.imaps.ssl.enable", true);
        props.put("mail.imaps.port", 993);
        props.put("mail.imaps.timeout", 5000);
        Session session = Session.getDefaultInstance(props);

        try {
            this.store = session.getStore("imaps");
        } catch (NoSuchProviderException e) {
            this.logger.error(e.getMessage());
        }
    }

    public void start() {
        try {

            for (int tries = 0; tries < 10; tries++) {
                try {
                    this.logger.info("[MailReceiverService] Trying go connect! (" + (tries + 1) + ")");
                    this.store.connect("imap.gmail.com", "fnctracker@gmail.com", password);
                    break;
                } catch (MessagingException ignored) {
                }
            }

            if (store.isConnected()) {
                this.logger.info("[MailReceiverService] Connected!");

                Folder inbox = store.getFolder("Inbox");
                inbox.open(Folder.READ_ONLY);
                inbox.addMessageCountListener(new MessageHandler(
                        this.userRepository,
                        this.spendingRepository,
                        this.mailSenderService));

                IdleThread idleThread = new IdleThread(inbox);
                idleThread.setDaemon(false);
                idleThread.start();
                idleThread.join();
                this.idleThread = idleThread;
            } else {
                this.logger.error("[MailReceiverService] Failed to connect!");
            }
        } catch (MessagingException | InterruptedException e) {
            this.logger.error(e.getMessage());
        }
    }

    @PreDestroy
    public void cleanUp() throws MessagingException {
        if (this.store != null && this.store.isConnected()) {
            this.store.close();
        }
        if (this.idleThread != null && this.idleThread.isRunning()) {
            this.idleThread.kill();
        }
    }

    private static class IdleThread extends Thread {
        private final Folder folder;
        private volatile boolean running = true;

        public IdleThread(Folder folder) {
            super();
            this.folder = folder;
        }

        public synchronized void kill() {
            if (!running)
                return;
            this.running = false;
        }

        public synchronized boolean isRunning() {
            return this.running;
        }

        @Override
        public void run() {
            while (running) {
                try {
                    ((IMAPFolder) folder).idle();
                } catch (Exception e) {
                    e.printStackTrace();
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ignored) {
                    }
                }
            }
        }
    }
}
