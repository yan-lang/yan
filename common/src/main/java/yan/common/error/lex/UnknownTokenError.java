package yan.common.error.lex;

import yan.foundation.compiler.frontend.lex.Lexer;
import yan.foundation.driver.error.Error;

public class UnknownTokenError extends Error {
    static String message = "unexpected character";

    public UnknownTokenError(Lexer lexer) {
        super(lexer.getLine(), lexer.getColumn() - 1, lexer.getCodeSource().getSourceName(),
                getMessage(lexer), lexer.getCodeSource().get(lexer.getLine()), null);
    }

    static String getMessage(Lexer lexer) {
        char c = lexer.getCodeSource().get(lexer.getLine()).charAt(lexer.getColumn() - 2);
        return message + " '" + c + "'.";
    }
}