package yan.foundation.compiler.frontend.lex;

public interface Lexer {
    Token nextToken();

    int getLine();

    int getColumn();

    Vocabulary getVocabulary();

    CodeSource getCodeSource();
}
