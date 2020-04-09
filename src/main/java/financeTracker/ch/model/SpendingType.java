package financeTracker.ch.model;

import com.fasterxml.jackson.annotation.JsonValue;

public enum SpendingType {
    SINGLE(0), SERIES(1);

    private final int type;

    SpendingType(int type) {
        this.type = type;
    }

    @JsonValue
    public int getType() {
        return type;
    }
}
