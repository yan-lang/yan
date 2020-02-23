package yan.skeleton.compiler.frontend.lex;

import java.io.IOException;
import java.io.InputStream;

public class ReadTextBuffer {

    // region Properties

    protected String source;
    protected int offset;       // current offset (in character)
    protected int length;       // total length of source code (in character)
    protected int line;         // current line
    protected int col;          // current column

    protected int previousLine;         // previous line
    protected int previousCol;          // previous column

    // endregion

    // region Public Methods

    public ReadTextBuffer(String source) throws IOException {
        this.source = source;
        offset = 0;
        line = 1;
        col = 1;
        length = source.length();
    }

    public char next() {
        if (offset < length) {
            updateLoc();
            offset += 1;
        }
        return current();
    }

    public char current() {
        return offset < length ? source.charAt(offset) : '\0';
    }

    public boolean current(char... chs) {
        char cur_ch = current();
        for (char ch : chs) {
            if (cur_ch == ch) {
                next();
                return true;
            }
        }
        return false;
    }

    public char peek() {
        return offset + 1 < length ? source.charAt(offset + 1) : '\0';
    }

    public boolean peek(char... chs) {
        char cur_ch = peek();
        for (char ch : chs) {
            if (cur_ch == ch) {
                next();
                return true;
            }
        }
        return false;
    }

    public String substring(int start, int end) {
        return source.substring(start, end);
    }

    // endregion

    // region Private Methods

    /**
     * Update current character's location
     **/
    private void updateLoc() {
        previousLine = line;
        previousCol = col;

        char ch = peek();
        if (ch == '\n') {
            line += 1;
            col = 0;
        }
        col += 1;
    }

    // endregion
}
