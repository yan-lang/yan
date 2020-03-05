package yan.examples.yan.parse;

import java.util.List;

public interface YanTokens {
    int UNKNOWN = 0;

    // 关键字 Keywords

    int IDENTIFIER = 1;

    // 数字
    int INT_CONST = 2;

    // 标点符号 Punctuator
    int PLUS = 3;
    int MINUS = 4;
    int MULTI = 5;
    int DIV = 6;
    int EXP = 7;
    int ASSIGN = 8;

    List<String> tokenNames = List.of("Unknown", "Identifier", "Integer Constant",
            "Plus+", "Minus-", "Multiply*", "Division/", "Exponential**", "Assign=");
}
