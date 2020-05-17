package yan.foundation.frontend.lex;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link Token#type}使用int类型, 在print Token的时候，我们需要将int转换成字符串,
 * 以获得更直观的类型信息, {@code Vocabulary}这个类即负责int到字符串的映射。
 */
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
        if (type == Token.EOF) {
            return "EOF";
        }
        if (type < 0 || type > getMaxTokenType()) {
            return String.valueOf(type);
        }
        return name.get(type);
    }
}
