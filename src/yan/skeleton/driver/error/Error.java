package yan.skeleton.driver.error;

import yan.skeleton.compiler.frontend.lex.Code;

public class Error extends BaseError{
    public Error(Code source, int line, int column, String message, String hint) {
        super(source, line, column, message, hint);
    }

    @Override
    public String getType() {
        return "error";
    }
}
