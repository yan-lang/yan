package yan.lang.predefine;

import yan.foundation.compiler.frontend.parse.AbstractParser;
import yan.foundation.driver.BaseConfig;
import yan.foundation.driver.PhaseFormatter;
import yan.lang.predefine.YanTree.BinaryOp;

import java.util.Optional;

public abstract class AbstractYanParser extends AbstractParser<YanTree.Program> implements YanTokens {
    PhaseFormatter<YanTree.Program> formatter = new ParseTreePrinter();

    public AbstractYanParser(String name, BaseConfig config) {
        super(name, config);
    }

    @Override
    public Optional<PhaseFormatter<? super YanTree.Program>> getFormatter() {
        return Optional.of(formatter);
    }

    protected void recovery() {
        while (!isAtEnd()) {
            if (previous().type == NEWLINE) return;
            if (previous().type == LEFT_BRACE) return;
            advance();
        }
    }

    protected BinaryOp getBinaryOp(int tokenType) {
        switch (tokenType) {
            case PLUS:
                return new BinaryOp(BinaryOp.Type.PLUS);
            case MINUS:
                return new BinaryOp(BinaryOp.Type.MINUS);
            case MULTI:
                return new BinaryOp(BinaryOp.Type.MULTI);
            case DIV:
                return new BinaryOp(BinaryOp.Type.DIV);
            case EXP:
                return new BinaryOp(BinaryOp.Type.EXP);
            case EQ:
                return new BinaryOp(BinaryOp.Type.EQUAL);
            case GT:
                return new BinaryOp(BinaryOp.Type.LARGE);
            case LT:
                return new BinaryOp(BinaryOp.Type.LESS);
        }
        throw new RuntimeException(String.format("Invalid token type {%d} for binary operator.", tokenType));
    }
}
