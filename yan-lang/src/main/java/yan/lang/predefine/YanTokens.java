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

    int SEMICOLON = 6;

    int IDENTIFIER = 7;

    int INT_CONST = 8;

    int PLUS = 9;

    int MINUS = 10;

    int MULTI = 11;

    int DIV = 12;

    int EXP = 13;

    int ASSIGN = 14;

    int LEFT_PAREN = 15;

    int RIGHT_PAREN = 16;

    int LEFT_BRACE = 17;

    int RIGHT_BRACE = 18;

    int LEFT_BRACKET = 19;

    int RIGHT_BRACKET = 20;

    int EQ = 21;

    int NEQ = 22;

    int GT = 23;

    int GTE = 24;

    int LT = 25;

    int LTE = 26;

    int REL_NOT = 27;

    int REL_OR = 28;

    int REL_AND = 29;

    int KW_VAR = 30;

    int KW_FUNC = 31;

    int KW_RETURN = 32;

    int KW_CLASS = 33;

    int KW_NEW = 34;

    int KW_IF = 35;

    int KW_ELSE = 36;

    int KW_WHILE = 37;

    int KW_CONTINUE = 38;

    int KW_BREAK = 39;

    int KW_TRUE = 40;

    int KW_FALSE = 41;


    List<String> tokenNames = List.of("Unknown character", "Newline", "colon", "comma", "arrow", "dot", "semicolon", "Identifier", "Integer constant", "plus", "minus", "multiply", "division", "exponential", "assign", "Left Parenthesis", "Right Parenthesis", "Left Brace", "Right Brace", "Left bracket", "right bracket", "Equal", "Not equal", "Greater than", "Greater than or equal", "Less than", "Less than or equal", "relation not", "relation or", "relation and", "Keyword var", "Keyword func", "Keyword return", "Keyword class", "Keyword new", "Keyword if", "Keyword else", "Keyword while", "Keyword continue", "Keyword break", "Keyword true", "Keyword false");

    List<String> tokenSymbolNames = List.of("Unknown character", "\\n", ":", "comma", "->", ".", ";", "Identifier", "Integer constant", "+", "-", "*", "/", "^", "assign", "(", ")", "{", "}", "[", "]", "Equal", "Not equal", ">", "Greater than or equal", "<", "Less than or equal", "!", "||", "&&", "var", "func", "return", "class", "new", "if", "else", "while", "continue", "break", "true", "false");

    Map<String, Integer> keywords = Map.ofEntries(Map.entry("var", 30), Map.entry("func", 31), Map.entry("return", 32), Map.entry("class", 33), Map.entry("new", 34), Map.entry("if", 35), Map.entry("else", 36), Map.entry("while", 37), Map.entry("continue", 38), Map.entry("break", 39), Map.entry("true", 40), Map.entry("false", 41));


}