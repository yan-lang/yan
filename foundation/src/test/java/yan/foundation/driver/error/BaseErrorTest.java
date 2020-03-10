package yan.foundation.driver.error;

import org.junit.jupiter.api.Test;
import yan.foundation.compiler.frontend.lex.CodeSource;
import yan.foundation.compiler.frontend.lex.ReadTextBuffer;

public class BaseErrorTest {
    @Test
    public void testToString() {
        CodeSource fakeSource = new ReadTextBuffer("print()", "test");
        BaseError error = new Error(fakeSource, 1, 7, "expect ; after expression", ";");
        System.out.println(error.toString());
    }
}
