package yan.foundation.compiler.frontend.parse;

import yan.foundation.compiler.frontend.ast.Tree;
import yan.foundation.compiler.frontend.lex.Token;
import yan.foundation.driver.lang.Phase;
import yan.foundation.driver.log.Diagnostic;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public abstract class AbstractParser<Out> extends Phase<List<Token>, Out> implements Parser<Out> {

    protected List<Token> tokens = new ArrayList<>();
    protected int current;

    public AbstractParser() {
        super();
    }

    public AbstractParser(String name) {
        super(name);
    }

    @Override
    public Out transform(List<Token> input) {
        return parse(input);
    }

    @Override
    public Out parse(List<Token> tokens) {
        this.tokens.clear();
        this.tokens.addAll(tokens);
        this.current = 0;
        return parse();
    }

    public abstract Out parse();


    // --------------------------------------------------------- //
    //      Helper Functions for filling tree node's range       //
    // --------------------------------------------------------- //


    protected <T extends Tree> T setRange(T node) {
        return setRange(node, current - 1, current - 1);
    }

    protected <T extends Tree> T setRange(T node, int from) {
        return setRange(node, from, current - 1);
    }

    protected <T extends Tree> T setRange(T node, int from, int to) {
        node.start = tokens.get(from);
        node.end = tokens.get(to);
        return node;
    }


    // ------------------------------------------------ //
    //      Helper Functions for accessing tokens       //
    // ------------------------------------------------ //

    protected Token consume(Supplier<Diagnostic> diagnosticSupplier, int... types) throws Diagnostic {
        for (int type : types) {
            if (check(type)) return advance();
        }
        throw diagnosticSupplier.get();
    }

    protected Token consume(int... types) throws Diagnostic {
        for (int type : types) {
            if (check(type)) return advance();
        }
        // we might expect some token at the beginning of a line,
        // under such circumstance, we should issue error message with "expect something before (the first token)"
        if (current().col == 1)
            throw BaseParserDiagnostic.Errors.ExpectationError(types2Str(types), current(), "before");
        else
            throw BaseParserDiagnostic.Errors.ExpectationError(types2Str(types), previous(), "after");
    }

    protected boolean match(int... types) {
        for (int type : types) {
            if (check(type)) {
                advance();
                return true;
            }
        }
        return false;
    }

    protected boolean check(int... types) {
        if (isAtEnd()) return false;
        for (int type : types) {
            if (current().type == type)
                return true;
        }
        return false;
    }

    protected boolean check(Token token, int... types) {
        for (int type : types) {
            if (token.type == type)
                return true;
        }
        return false;
    }

    protected Token LA(int x) {
        // TODO(Important): 如果index out of bound应该如何处理比较好
        return tokens.get(current + x);
    }

    protected Token current() {
        return tokens.get(current);
    }

    protected Token previous() {
        return tokens.get(Integer.max(current - 1, 0));
    }

    protected Token advance() {
        if (!isAtEnd()) current++;
        return previous();
    }

    protected void back() {
        current -= 1;
    }

    protected boolean isAtEnd() {
        return current >= tokens.size() - 1;
    }

    // ------------------------------------------------ //
    //       Helper functions for throwing errors       //
    // ------------------------------------------------ //

    protected interface TokenTypeStringMapper {
        String toString(int tokenType);
    }

    protected abstract TokenTypeStringMapper getTokenTypeStringMapper();

    protected String type2Str(int tokenType) {
        // Note: EOF(-1) need special handler, it is not token specified by token file.
        if (tokenType == Token.EOF) return "EOF";
        return getTokenTypeStringMapper().toString(tokenType);
    }

    protected String types2Str(int... tokenTypes) {
        List<String> strs = new ArrayList<>();
        for (int type : tokenTypes) {
            strs.add(String.format("'%s'", type2Str(type)));
        }
        if (strs.size() > 1) return "[" + String.join(",", strs) + "]";
        else return String.join(",", strs);
    }
}
