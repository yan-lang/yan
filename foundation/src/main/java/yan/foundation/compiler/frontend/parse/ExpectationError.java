package yan.foundation.compiler.frontend.parse;

import yan.foundation.compiler.frontend.lex.Token;
import yan.foundation.driver.error.Error;

public class ExpectationError extends Error {
    static public String AFTER = "after";
    static public String BEFORE = "before";

    static String template = "expect %s %s %s.";

    public ExpectationError(String expectation, Token anchor, String pos) {
        super(anchor.source, anchor.line, anchor.col,
                String.format(template, expectation, pos, anchor.getText()), expectation);
    }

}
