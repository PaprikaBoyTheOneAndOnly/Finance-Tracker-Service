package financeTracker.ch.pesrsistence;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column
    private String email;
    @Column
    private String password;

    @OneToMany(mappedBy = "creator")
    private List<Spending> spendings;

    public User() {
        this(0, "", "", new ArrayList<>());
    }

    public User(String email, String password) {
        this(0, email, password, new ArrayList<>());
    }

    public User(int id, String email, String password, List<Spending> spendings) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.spendings = spendings;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Spending> getSpendings() {
        return this.spendings;
    }

    public void setSpendings(List<Spending> spendings) {
        this.spendings = spendings;
    }
}
