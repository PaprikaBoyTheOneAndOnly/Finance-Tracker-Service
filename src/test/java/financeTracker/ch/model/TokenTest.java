package financeTracker.ch.model;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class TokenTest {
    @Test
    public void testConstructor() {
        Token token = new Token("Bearer itsAToken");
        assertThat(token.getValue(), is("itsAToken"));

        Token token2 = new Token("itsAnotherToken");
        assertThat(token2.getValue(), is("itsAnotherToken"));
    }
}
