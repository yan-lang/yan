package yan.foundation.driver.error;

import yan.foundation.compiler.frontend.lex.CodeSource;

public class Warning extends BaseError {
    public Warning(CodeSource source, int line, int column, String message, String hint) {
        super(source, line, column, message, hint);
    }

    @Override
    public String getType() {
        return "warning";
    }
}
