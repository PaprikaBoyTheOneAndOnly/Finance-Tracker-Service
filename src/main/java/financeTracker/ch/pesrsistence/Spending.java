package financeTracker.ch.pesrsistence;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import financeTracker.ch.model.SpendingType;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class Spending {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column
    private double amount;
    @Column
    private String description;
    @Column
    private LocalDate date;
    @Column
    private SpendingType type;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User creator;

    public Spending() {
        this(0, 0, "", null, SpendingType.SINGLE);
    }

    public Spending(double amount, String description, LocalDate date, SpendingType type) {
        this(0, amount, description, date, type);
    }

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
        return this.amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public SpendingType getType() {
        return this.type;
    }

    public void setType(SpendingType type) {
        this.type = type;
    }

    public User getCreator() {
        return this.creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }
}
