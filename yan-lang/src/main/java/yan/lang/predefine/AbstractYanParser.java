package yan.lang.predefine;

import yan.foundation.compiler.frontend.lex.Token;
import yan.foundation.compiler.frontend.parse.AbstractParser;

import java.util.Set;

public abstract class AbstractYanParser extends AbstractParser<YanTree.Program> implements YanTokens {

    @Override
    protected TokenTypeStringMapper getTokenTypeStringMapper() { return tokenSymbolNames::get; }

    protected void recovery() {
        advance();
        while (!isAtEnd()) {
            if (previous().type == NEWLINE) return;
            if (previous().type == LEFT_BRACE) return;
            advance();
        }
    }

    protected YanTree.Operator.Tag getOperatorTag(Token token) {
        return switch (token.type) {
            case PLUS -> YanTree.Operator.Tag.PLUS;
            case MINUS -> YanTree.Operator.Tag.MINUS;
            case MULTI -> YanTree.Operator.Tag.MULTI;
            case DIV -> YanTree.Operator.Tag.DIV;
            case EXP -> YanTree.Operator.Tag.EXP;
            case ASSIGN -> YanTree.Operator.Tag.ASSIGN;
            case EQ -> YanTree.Operator.Tag.EQ;
            case NEQ -> YanTree.Operator.Tag.NEQ;
            case GT -> YanTree.Operator.Tag.GT;
            case GTE -> YanTree.Operator.Tag.GTE;
            case LT -> YanTree.Operator.Tag.LT;
            case LTE -> YanTree.Operator.Tag.LTE;
            case REL_OR -> YanTree.Operator.Tag.REL_OR;
            case REL_AND -> YanTree.Operator.Tag.REL_AND;
            case REL_NOT -> YanTree.Operator.Tag.REL_NOT;
            default -> throw new IllegalStateException(token.getText() + " is not a operator token.");
        };
    }

    protected final Set<Integer> typeTokenTypes = Set.of(KW_INT, IDENTIFIER);

    protected boolean isTypeToken(Token token) {
        return typeTokenTypes.contains(token.type);
    }

}
