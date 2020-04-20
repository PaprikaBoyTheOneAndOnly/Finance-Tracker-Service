package financeTracker.ch.model;

import financeTracker.ch.pesrsistence.Spending;

import java.time.*;
import java.time.format.DateTimeFormatter;

public class RESTSpending {
    private Spending spending;

    public RESTSpending() {
        this.spending = new Spending();
    }

    public RESTSpending(Spending spending) {
        this.spending = spending;
    }

    public int getId() {
        return spending.getId();
    }

    public void setId(int id) {
        this.spending.setId(id);
    }

    public double getAmount() {
        return this.spending.getAmount();
    }

    public void setAmount(double amount) {
        this.spending.setAmount(amount);
    }

    public String getDescription() {
        return this.spending.getDescription();
    }

    public void setDescription(String description) {
        this.spending.setDescription(description);
    }

    public String getDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        return formatter.format(LocalDateTime.of(this.spending.getDate(), LocalTime.of(1,0)));
    }

    public void setDate(String date) {
        Instant instant = Instant.parse(date);
        this.spending.setDate(LocalDateTime
                .ofInstant(instant, ZoneId.systemDefault())
                .toLocalDate());
    }

    public LocalDate parsedDate() {
        return this.spending.getDate();
    }

    public SpendingType getType() {
        return this.spending.getType();
    }

    public void setType(SpendingType type) {
        this.spending.setType(type);
    }
}
