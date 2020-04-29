package yan.common.diagnostic;

import yan.foundation.compiler.frontend.lex.Token;
import yan.foundation.driver.log.Diagnostic;

public interface LexErrors {
    default Diagnostic UnknownToken(Token token) {
        Diagnostic d = Diagnostic.Error("unexpected token");
        Errors.fillRangePosition(token, d, true);
        return d;
    }
}
