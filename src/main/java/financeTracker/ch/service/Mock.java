package financeTracker.ch.service;

import financeTracker.ch.model.Spending;
import financeTracker.ch.model.SpendingType;
import financeTracker.ch.model.User;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class Mock {
    public final static List<User> mockUsers = Arrays.asList(
            new User(0, "user@gmail.com", "12345", Arrays.asList(
                    new Spending(1, 60, "sunrise bill", LocalDate.now(), SpendingType.SINGLE),
                    new Spending(2, 20, "Skillspark Abo", LocalDate.now(), SpendingType.SINGLE),
                    new Spending(3, 25, "Xbox Live", LocalDate.now(), SpendingType.SINGLE))),
            new User(1, "user@gmail.com", "12345", Arrays.asList(
                    new Spending(4, 15000, "Hookers & Cocaine", LocalDate.now(), SpendingType.SINGLE),
                    new Spending(5, 420, "Mom\'s Spaghetti", LocalDate.now(), SpendingType.SINGLE),
                    new Spending(6, 69, "The coochie Fund", LocalDate.now(), SpendingType.SINGLE)))
    );

}
