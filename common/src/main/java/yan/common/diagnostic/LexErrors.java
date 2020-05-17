package yan.common.diagnostic;

import yan.foundation.driver.log.Diagnostic;
import yan.foundation.frontend.lex.Token;

public interface LexErrors {
    default Diagnostic UnknownToken(Token token) {
        Diagnostic d = Diagnostic.Error(String.format("unexpected token '%s'", token.getText()));
        Errors.fillRangePosition(token, d, true);
        return d;
    }

    default Diagnostic UnTerminatedString(Token token) {
        Diagnostic d = Diagnostic.Error("unterminated string");
        Errors.fillRangePosition(token, d, true);
        return d;
    }

    default Diagnostic UnTerminatedCharacter(Token token) {
        Diagnostic d = Diagnostic.Error("unterminated character");
        Errors.fillRangePosition(token, d, true);
        return d;
    }

}
