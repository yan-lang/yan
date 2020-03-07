package yan.foundation.driver.error;

import yan.foundation.compiler.frontend.lex.CodeSource;

public class Error extends BaseError{
    public Error(CodeSource source, int line, int column, String message, String hint) {
        super(source, line, column, message, hint);
    }

    @Override
    public String getType() {
        return "error";
    }
}
