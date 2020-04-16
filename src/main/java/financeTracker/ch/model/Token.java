package financeTracker.ch.model;

import java.util.Objects;

public class Token {
    private String value;
    private int userId;

    public Token() {
        this("", 0);
    }

    public Token(String value) {
        this(value, 0);
    }

    public Token(String value, int userId) {
        if (value.contains("Bearer")) {
            value = value.replace("Bearer ", "");
        }
        this.value = value;
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Token)) return false;
        Token token = (Token) o;
        return Objects.equals(value, token.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
