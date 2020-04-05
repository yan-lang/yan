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

    int IDENTIFIER = 5;

    int INT_CONST = 6;

    int PLUS = 7;

    int MINUS = 8;

    int MULTI = 9;

    int DIV = 10;

    int EXP = 11;

    int ASSIGN = 12;

    int LEFT_PAREN = 13;

    int RIGHT_PAREN = 14;

    int LEFT_BRACE = 15;

    int RIGHT_BRACE = 16;

    int LEFT_BRACKET = 17;

    int RIGHT_BRACKET = 18;

    int EQ = 19;

    int NEQ = 20;

    int GT = 21;

    int GTE = 22;

    int LT = 23;

    int LTE = 24;

    int REL_NOT = 25;

    int REL_OR = 26;

    int REL_AND = 27;

    int KW_FUNC = 28;

    int KW_VAR = 29;

    int KW_CLASS = 30;

    int KW_NEW = 31;

    int KW_IF = 32;

    int KW_ELSE = 33;

    int KW_WHILE = 34;

    int KW_PRINT = 35;

    int KW_READ = 36;

    int KW_TRUE = 37;

    int KW_FALSE = 38;

    int KW_INT = 39;


    List<String> tokenNames = List.of("Unknown character", "Newline", "colon", "comma", "arrow", "Identifier", "Integer constant", "plus", "minus", "multiply", "division", "exponential", "assign", "Left Parenthesis", "Right Parenthesis", "Left Brace", "Right Brace", "Left bracket", "right bracket", "Equal", "Not equal", "Greater than", "Greater than or equal", "Less than", "Less than or equal", "relation not", "relation or", "relation and", "Keyword func", "Keyword var", "Keyword class", "Keyword new", "Keyword if", "Keyword else", "Keyword while", "Keyword print", "keyword read", "Keyword true", "Keyword false", "Keyword int");

    List<String> tokenSymbolNames = List.of("Unknown character", "\n", ":", "comma", "->", "Identifier", "Integer constant", "+", "-", "*", "/", "^", "assign", "(", ")", "{", "}", "[", "]", "Equal", "Not equal", ">", "Greater than or equal", "<", "Less than or equal", "!", "||", "&&", "func", "var", "class", "new", "if", "else", "while", "print", "read", "true", "false", "int");

    Map<String, Integer> keywords = Map.ofEntries(Map.entry("func", 28), Map.entry("var", 29), Map.entry("class", 30), Map.entry("new", 31), Map.entry("if", 32), Map.entry("else", 33), Map.entry("while", 34), Map.entry("print", 35), Map.entry("read", 36), Map.entry("true", 37), Map.entry("false", 38), Map.entry("int", 39));


}