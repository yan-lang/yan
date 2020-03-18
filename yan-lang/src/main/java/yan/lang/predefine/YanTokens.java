package yan.lang.predefine;

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

    int KW_IF = 11;

    int KW_ELSE = 12;

    int LEFT_PAREN = 13;

    int RIGHT_PAREN = 14;

    int LEFT_BRACE = 15;

    int RIGHT_BRACE = 16;

    int EQUAL = 17;

    int LARGER = 18;

    int LESS = 19;

    int KW_TRUE = 20;

    int KW_FALSE = 21;

    int KW_PRINT = 22;

    List<String> tokenNames = List.of("Unknown character", "Identifier", "Keyword var", "Integer constant",
            "Punctuator plus+", "Punctuator minus-", "Punctuator multiply*", "Punctuator division/",
            "Punctuator exponential**", "Punctuator assign=", "Newline", "Keyword if", "Keyword else",
            "Left Parenthesis(", "Right Parenthesis)", "Left Brace{", "Right Brace}", "Equal==", "Larger>",
            "Less<", "Keyword true", "Keyword false", "Keyword print");

    Map<String, Integer> keywords = Map.of("var", 2, "if", 11, "else", 12,
            "true", 20, "false", 21, "print", 22);
}