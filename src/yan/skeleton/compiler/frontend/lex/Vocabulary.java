package yan.skeleton.compiler.frontend.lex;

import java.util.ArrayList;
import java.util.List;

public class Vocabulary {
    List<String> name;

    public Vocabulary(List<String> name) {
        this.name = name;
    }

    public Vocabulary() {
        name = new ArrayList<>();
    }

    public int getMaxTokenType() {
        return name.size() - 1;
    }

    String get(int type) {
        if (type == LexerToken.EOF) {
            return "EOF";
        }
        if (type < 0 || type > getMaxTokenType()) {
            return String.valueOf(type);
        }
        return name.get(type);
    }
}
