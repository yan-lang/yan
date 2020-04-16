package yan.foundation.compiler.frontend.parse;

import yan.foundation.compiler.frontend.lex.Token;
import yan.foundation.driver.log.Diagnostic;

public class BaseParserDiagnostic {
    public static IErrors Errors = new IErrors() {
    };

    public interface IErrors {
        default Diagnostic ExpectationError(String expectation, Token anchor, String pos) {
            Diagnostic diagnostic = Diagnostic.Error(String.format("expect %s %s \"%s\".", expectation, pos, anchor.getText()));
            diagnostic.line = anchor.line;
            diagnostic.column = anchor.col;
            diagnostic.sourceName = anchor.source.getSourceName();
            diagnostic.context = anchor.source.get(anchor.line);
            diagnostic.hint = expectation;
            return diagnostic;
        }
    }
}
