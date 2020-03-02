package yan.skeleton.compiler.frontend.lex;

public interface CodeSource {
    String getSourceName();

    String getContent();

    String getContent(int line);
}
