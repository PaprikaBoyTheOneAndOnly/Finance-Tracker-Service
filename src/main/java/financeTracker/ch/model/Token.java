package financeTracker.ch.model;

import java.util.Objects;

public class Token {
    private String value;

    public Token() {
    }

    public Token(String value) {
        if(value.contains("Bearer")) {
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
    public int hashCode() {
        return Objects.hash(value);
    }
}
