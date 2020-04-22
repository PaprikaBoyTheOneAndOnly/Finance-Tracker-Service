package financeTracker.ch.model;

import java.util.Objects;

public class Token {
    private String value;

    public Token() {
        this("");
    }

    public Token(String value) {
        if (value.contains("Bearer")) {
            value = value.replace("Bearer ", "");
        }
        this.value = value;
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
