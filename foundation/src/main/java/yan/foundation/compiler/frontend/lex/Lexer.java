package yan.foundation.compiler.frontend.lex;

public interface Lexer {
    LexerToken nextToken();

    Vocabulary getVocabulary();
}
