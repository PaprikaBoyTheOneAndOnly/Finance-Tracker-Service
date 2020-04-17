package financeTracker.ch.mail;

import financeTracker.ch.model.SpendingType;
import financeTracker.ch.pesrsistence.Spending;
import financeTracker.ch.pesrsistence.SpendingRepository;
import financeTracker.ch.pesrsistence.User;
import financeTracker.ch.pesrsistence.UserRepository;
import financeTracker.ch.service.MailSenderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.event.MessageCountEvent;
import javax.mail.event.MessageCountListener;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Optional;


public class MessageHandler implements MessageCountListener {
    private final Logger logger;
    private final UserRepository userRepository;
    private final SpendingRepository spendingRepository;
    private final MailSenderService mailSenderService;

    public MessageHandler(UserRepository userRepository,
                          SpendingRepository spendingRepository,
                          MailSenderService mailSenderService) {
        this.userRepository = userRepository;
        this.spendingRepository = spendingRepository;
        this.mailSenderService = mailSenderService;
        this.logger = LoggerFactory.getLogger(MessageCountListener.class);
    }

    @Override
    public void messagesAdded(MessageCountEvent messageCountEvent) {
        Message[] messages = messageCountEvent.getMessages();

        for (Message message : messages) {
            this.logger.info("[MessageHandler] (" + message + ") Message received!");
            try {
                String email = extractEmail(message.getFrom()[0].toString());
                this.logger.info("[MessageHandler] (" + message + ") Message from " + email);
                Optional<User> user = this.userRepository.findByEmail(email);

                if (user.isPresent()) {
                    if (this.saveNewSpending(message, user.get())) {
                        this.mailSenderService.sendEmail(email,
                                "Your spending was added to your list.");
                    }
                } else {
                    this.logger.info("[MessageHandler] User not available!");
                    this.mailSenderService.sendEmail(email,
                            "No account could be found for ["
                                    + email
                                    + "]!\nPlease create an account first to add a spending to your list.");
                }
            } catch (IOException | MessagingException e) {
                this.logger.error(e.getMessage());
            }
        }
    }

    private boolean saveNewSpending(Message message, User user) throws IOException, MessagingException {
        try {
            String content = (String) message.getContent();
            String description = message.getSubject();
            LocalDate date = extractDate(content);
            double amount = extractAmount(content);
            Spending spending = new Spending(amount, description, date, SpendingType.SINGLE);
            spending.setCreator(user);

            this.spendingRepository.save(spending);
            return true;
        } catch (UnsupportedMessageFormatException e) {
            this.logger.error("[MessageHandler] Error while reading message content!");
            this.logger.error(e.getMessage());
            this.mailSenderService.sendEmail(user.getEmail(),
                    "An error occurred while adding a spending to your list!\n" +
                            e.getMessage());
            return false;
        }
    }

    private LocalDate extractDate(String content) {
        content = content.toLowerCase();

        if (content.contains("date:")) {
            int index = content.indexOf("date: ") + 6;
            try {
                return LocalDate.parse(content.substring(index, index + 10));
            } catch (DateTimeParseException ignored) {
            }
        }

        return LocalDate.now();
    }

    private double extractAmount(String content) throws UnsupportedMessageFormatException {
        content = content.toLowerCase();
        if (!content.contains("amount")) {
            throw new UnsupportedMessageFormatException("Amount is not defined!");
        }

        try {
            int endIndex = content.contains(" chf") ?
                    content.indexOf(" chf") :
                    content.indexOf("chf");
            content = content.substring(content.indexOf("amount: ") + 8, endIndex);
            return Double.parseDouble(content);
        } catch (IndexOutOfBoundsException | NumberFormatException ignored) {
            throw new UnsupportedMessageFormatException("Amount has has wrong format!");
        }
    }

    private String extractEmail(String from) {
        return from.contains("<") ?
                from.substring(from.indexOf("<") + 1, from.indexOf(">")).trim() :
                from;
    }

    @Override
    public void messagesRemoved(MessageCountEvent e) {
    }
}
