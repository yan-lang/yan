package yan.lang.predefine;

import yan.foundation.compiler.frontend.lex.Token;

import java.util.List;
import java.util.Map;

public interface YanTokens {
    int EOF = Token.EOF;

    int UNKNOWN = 0;

    int NEWLINE = 1;

    int COLON = 2;

    int COMMA = 3;

    int ARROW = 4;

    int DOT = 5;

    int IDENTIFIER = 6;

    int INT_CONST = 7;

    int PLUS = 8;

    int MINUS = 9;

    int MULTI = 10;

    int DIV = 11;

    int EXP = 12;

    int ASSIGN = 13;

    int LEFT_PAREN = 14;

    int RIGHT_PAREN = 15;

    int LEFT_BRACE = 16;

    int RIGHT_BRACE = 17;

    int LEFT_BRACKET = 18;

    int RIGHT_BRACKET = 19;

    int EQ = 20;

    int NEQ = 21;

    int GT = 22;

    int GTE = 23;

    int LT = 24;

    int LTE = 25;

    int REL_NOT = 26;

    int REL_OR = 27;

    int REL_AND = 28;

    int KW_FUNC = 29;

    int KW_VAR = 30;

    int KW_CLASS = 31;

    int KW_NEW = 32;

    int KW_IF = 33;

    int KW_ELSE = 34;

    int KW_WHILE = 35;

    int KW_PRINT = 36;

    int KW_READ = 37;

    int KW_TRUE = 38;

    int KW_FALSE = 39;


    List<String> tokenNames = List.of("Unknown character", "Newline", "colon", "comma", "arrow", "dot", "Identifier", "Integer constant", "plus", "minus", "multiply", "division", "exponential", "assign", "Left Parenthesis", "Right Parenthesis", "Left Brace", "Right Brace", "Left bracket", "right bracket", "Equal", "Not equal", "Greater than", "Greater than or equal", "Less than", "Less than or equal", "relation not", "relation or", "relation and", "Keyword func", "Keyword var", "Keyword class", "Keyword new", "Keyword if", "Keyword else", "Keyword while", "Keyword print", "keyword read", "Keyword true", "Keyword false");

    List<String> tokenSymbolNames = List.of("Unknown character", "\n", ":", "comma", "->", ".", "Identifier", "Integer constant", "+", "-", "*", "/", "^", "assign", "(", ")", "{", "}", "[", "]", "Equal", "Not equal", ">", "Greater than or equal", "<", "Less than or equal", "!", "||", "&&", "func", "var", "class", "new", "if", "else", "while", "print", "read", "true", "false");

    Map<String, Integer> keywords = Map.ofEntries(Map.entry("func", 29), Map.entry("var", 30), Map.entry("class", 31), Map.entry("new", 32), Map.entry("if", 33), Map.entry("else", 34), Map.entry("while", 35), Map.entry("print", 36), Map.entry("read", 37), Map.entry("true", 38), Map.entry("false", 39));


}