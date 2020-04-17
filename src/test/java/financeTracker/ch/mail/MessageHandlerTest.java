package financeTracker.ch.mail;

import financeTracker.ch.model.SpendingType;
import financeTracker.ch.pesrsistence.Spending;
import financeTracker.ch.pesrsistence.SpendingRepository;
import financeTracker.ch.pesrsistence.User;
import financeTracker.ch.pesrsistence.UserRepository;
import financeTracker.ch.service.MailSenderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.event.MessageCountEvent;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;


@ActiveProfiles("Test")
@ExtendWith(MockitoExtension.class)
public class MessageHandlerTest {
    private MessageHandler testee;

    private SpendingRepository mockSpendingRepository;
    private UserRepository mockUserRepository;
    private Message mockMessage;
    private MessageCountEvent mockMessageCountEvent;
    private Address mockAddress;
    private MailSenderService mockMailSenderService;

    @BeforeEach
    public void setUp(@Mock SpendingRepository mockSpendingRepository,
                      @Mock UserRepository mockUserRepository,
                      @Mock Message mockMessage,
                      @Mock MessageCountEvent mockMessageCountEvent,
                      @Mock Address mockAddress,
                      @Mock MailSenderService mockMailSenderService) throws MessagingException, IOException {
        this.mockSpendingRepository = mockSpendingRepository;
        this.mockUserRepository = mockUserRepository;
        this.mockMessage = mockMessage;
        this.mockAddress = mockAddress;
        this.mockMessageCountEvent = mockMessageCountEvent;
        this.mockMailSenderService = mockMailSenderService;
        this.testee = new MessageHandler(mockUserRepository, mockSpendingRepository, mockMailSenderService);

        when(mockAddress.toString()).thenReturn("peter@gmail.com");
        lenient().when(mockMessage.getContent()).thenReturn("Amount: -12.50 CHF");
        lenient().when(mockMessage.getSubject()).thenReturn("Zmittag Coop");
        when(mockMessage.getFrom()).thenReturn(new Address[]{mockAddress});
        when(mockMessageCountEvent.getMessages()).thenReturn(new Message[]{this.mockMessage});
        when(mockUserRepository.findByEmail(anyString()))
                .thenReturn(Optional.of(new User(0, "peter@gmail.com", "aPassword", null)));
    }

    @Test
    public void testMessageAdded() {
        this.testee.messagesAdded(this.mockMessageCountEvent);

        ArgumentCaptor<Spending> spendingCaptor = ArgumentCaptor.forClass(Spending.class);
        verify(this.mockSpendingRepository, times(1)).save(spendingCaptor.capture());
        verify(this.mockMailSenderService, times(1))
                .sendEmail("peter@gmail.com", "Your spending was added to your list.");

        Spending addedSpending = spendingCaptor.getValue();
        assertThat(addedSpending.getAmount(), is(-12.5));
        assertThat(addedSpending.getDescription(), is("Zmittag Coop"));
        assertThat(addedSpending.getDate(), is(LocalDate.now()));
        assertThat(addedSpending.getType(), is(SpendingType.SINGLE));
    }

    @Test
    public void testMessageAdded_emailHasName() {
        when(mockAddress.toString()).thenReturn("<Peter Parker> peter@gmail.com");

        this.testee.messagesAdded(this.mockMessageCountEvent);

        verify(this.mockUserRepository, times(1)).findByEmail("peter@gmail.com");
    }

    @Test
    public void testMessageAdded_noUserFound() {
        when(this.mockUserRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        this.testee.messagesAdded(this.mockMessageCountEvent);

        verify(this.mockMailSenderService, times(1))
                .sendEmail("peter@gmail.com", "No account could be found for [peter@gmail.com]!" +
                        "\nPlease create an account first to add a spending to your list.");
    }

    @Test
    public void testMessageAdded_dateGiven() throws IOException, MessagingException {
        when(this.mockMessage.getContent()).thenReturn("Amount: 12.4 chf\nDate: 2020-04-17");

        this.testee.messagesAdded(this.mockMessageCountEvent);

        ArgumentCaptor<Spending> spendingCaptor = ArgumentCaptor.forClass(Spending.class);
        verify(this.mockSpendingRepository, times(1)).save(spendingCaptor.capture());

        Spending addedSpending = spendingCaptor.getValue();
        assertThat(addedSpending.getDate(), is(LocalDate.of(2020, 4, 17)));
    }

    @Test
    public void testMessageAdded_dateGivenButWrongFormat() throws IOException, MessagingException {
        when(this.mockMessage.getContent()).thenReturn("Amount: 12.4 chf\nDate: 17-04-2020");

        this.testee.messagesAdded(this.mockMessageCountEvent);

        ArgumentCaptor<Spending> spendingCaptor = ArgumentCaptor.forClass(Spending.class);
        verify(this.mockSpendingRepository, times(1)).save(spendingCaptor.capture());

        Spending addedSpending = spendingCaptor.getValue();
        assertThat(addedSpending.getDate(), is(LocalDate.now()));
    }

    @Test
    public void testMessageAdded_secondPossibleAmountFormat() throws IOException, MessagingException {
        when(this.mockMessage.getContent()).thenReturn("Amount: -12.40chf");

        this.testee.messagesAdded(this.mockMessageCountEvent);

        ArgumentCaptor<Spending> spendingCaptor = ArgumentCaptor.forClass(Spending.class);
        verify(this.mockSpendingRepository, times(1)).save(spendingCaptor.capture());

        Spending addedSpending = spendingCaptor.getValue();
        assertThat(addedSpending.getAmount(), is(-12.4));
    }

    @Test
    public void testMessageAdded_noAmountGiven() throws IOException, MessagingException {
        when(this.mockMessage.getContent()).thenReturn("Invalid content");

        this.testee.messagesAdded(this.mockMessageCountEvent);

        verify(this.mockMailSenderService, times(1))
                .sendEmail("peter@gmail.com",
                        "An error occurred while adding a spending to your list!\nAmount is not defined!");
    }

    @Test
    public void testMessageAdded_wrongAmountFormat() throws IOException, MessagingException {
        when(this.mockMessage.getContent()).thenReturn("Amount: notANumber");

        this.testee.messagesAdded(this.mockMessageCountEvent);

        verify(this.mockMailSenderService, times(1))
                .sendEmail("peter@gmail.com",
                        "An error occurred while adding a spending to your list!\nAmount has has wrong format!");
    }

    @Test
    public void testMessageAdded_noCHFEndingGiven() throws IOException, MessagingException {
        when(this.mockMessage.getContent()).thenReturn("Amount: 12");

        this.testee.messagesAdded(this.mockMessageCountEvent);

        verify(this.mockMailSenderService, times(1))
                .sendEmail("peter@gmail.com",
                        "An error occurred while adding a spending to your list!\nAmount has has wrong format!");
    }
}
