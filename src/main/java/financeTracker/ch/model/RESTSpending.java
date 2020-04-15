package financeTracker.ch.model;

import financeTracker.ch.pesrsistence.Spending;

import java.time.LocalDate;

public class RESTSpending {
    private Spending spending;

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

    public LocalDate getDate() {
        return this.spending.getDate();
    }

    public void setDate(LocalDate date) {
        this.spending.setDate(date);
    }

    public SpendingType getType() {
        return this.spending.getType();
    }

    public void setType(SpendingType type) {
        this.spending.setType(type);
    }
}
