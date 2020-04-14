package financeTracker.ch.model;

import java.util.List;

public class User {
    private int id;
    private String email;
    private String password;
    private List<Spending> spendings;

    public User(int id, String email, String password, List<Spending> spendings) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.spendings = spendings;
    }

    public List<Spending> getSpendings() {
        return spendings;
    }

    public void setSpendings(List<Spending> spendings) {
        this.spendings = spendings;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
