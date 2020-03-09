package yan.foundation.compiler.frontend.lex;

public interface Lexer {
    Token nextToken();

    Vocabulary getVocabulary();
}
