package yan.lang;

import yan.foundation.compiler.frontend.lex.Token;

import java.util.List;
import java.util.Map;

public interface YanTokens {
    int EOF = Token.EOF;

    int UNKNOWN = 0;

    int IDENTIFIER = 1;

    int KW_VAR = 2;

    int INT_CONST = 3;

    int PLUS = 4;

    int MINUS = 5;

    int MULTI = 6;

    int DIV = 7;

    int EXP = 8;

    int ASSIGN = 9;

    int NEWLINE = 10;

    List<String> tokenNames = List.of("Unknown character", "Identifier", "Keyword var", "Integer constant",
            "Punctuator plus+", "Punctuator minus-", "Punctuator multiply*", "Punctuator division/",
            "Punctuator exponential**", "Punctuator assign=", "Newline");

    Map<String, Integer> keywords = Map.of("var", 2);
}