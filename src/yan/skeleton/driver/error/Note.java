package yan.skeleton.driver.error;

import yan.skeleton.compiler.frontend.lex.CodeSource;

public class Note extends BaseError{
    public Note(CodeSource source, int line, int column, String message, String hint) {
        super(source, line, column, message, hint);
    }

    @Override
    String getType() {
        return "note";
    }
}