package financeTracker.ch.model;

import java.time.LocalDate;

public class Spending {
    private int id;
    private double amount;
    private String description;
    private LocalDate date;
    private SpendingType type;

    public Spending(int id, double amount, String description, LocalDate date, SpendingType type) {
        this.id = id;
        this.amount = amount;
        this.description = description;
        this.date = date;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public SpendingType getType() {
        return type;
    }

    public void setType(SpendingType type) {
        this.type = type;
    }
}
