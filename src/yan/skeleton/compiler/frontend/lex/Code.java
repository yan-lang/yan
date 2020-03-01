package yan.skeleton.compiler.frontend.lex;

public interface Code {
    String getSourceName();

    String getContent();

    String getContent(int line);
}
