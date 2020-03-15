package yan.foundation.driver.error;

import org.junit.jupiter.api.Test;
import yan.foundation.compiler.frontend.lex.CodeSource;
import yan.foundation.compiler.frontend.lex.ReadTextBuffer;

public class UnexpectedTest {
    @Test
    public void testToString() {
        CodeSource fakeSource = new ReadTextBuffer("print()", "test");
        Unexpected error = new Error(1, 7, fakeSource.getSourceName(),
                "expect ; after expression", fakeSource.get(1), ";");
        System.out.println(error.toString());
    }
}
