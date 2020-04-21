package financeTracker.ch.model;

import financeTracker.ch.pesrsistence.User;

public class RESTUser {
    private User user;

    public RESTUser() {
        this(new User());
    }

    public RESTUser(User user) {
        this.user = user;
    }

    public int getId() {
        return this.user.getId();
    }

    public void setId(int id) {
        this.user.setId(id);
    }

    public String getEmail() {
        return this.user.getEmail();
    }

    public void setEmail(String email) {
        this.user.setEmail(email);
    }

    public String getPassword() {
        return this.user.getPassword();
    }

    public void setPassword(String password) {
        this.user.setPassword(password);
    }
}
