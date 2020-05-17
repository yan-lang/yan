package yan.lang.predefine.abc;

import yan.foundation.driver.log.Diagnostic;
import yan.foundation.frontend.ast.Tree;
import yan.foundation.frontend.lex.Token;
import yan.foundation.frontend.parse.AbstractParser;
import yan.foundation.frontend.parse.BaseParserDiagnostic;
import yan.lang.predefine.YanTokens;
import yan.lang.predefine.YanTree;

public abstract class AbstractYanParser extends AbstractParser<YanTree.Program> implements YanTokens {

    /**
     * 朴素的出错恢复处理方法: 跳过出错Token之后的所有除了换行和右大括号的Token,
     * 然后消耗掉遇到的第一个换行或大括号。
     */
    protected void recovery() {
        while (!isAtEnd()) {
            if (current().type == NEWLINE) break;
            if (current().type == RIGHT_BRACE) break;
            advance();
        }
        advance();
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
            case LOR -> YanTree.Operator.Tag.LOR;
            case LAND -> YanTree.Operator.Tag.LAND;
            case LNOT -> YanTree.Operator.Tag.LNOT;
            default -> throw new IllegalStateException(token.getText() + " is not a operator token.");
        };
    }

    @Override
    protected TokenTypeStringMapper getTokenTypeStringMapper() { return tokenSymbolNames::get; }

    // ------- Errors that might be thrown in this phase ------- //

    public static IErrors Errors = new IErrors() {
    };

    public interface IErrors extends BaseParserDiagnostic.IErrors {

        default Diagnostic ConsecutiveStatements(Tree tree) {
            Diagnostic d = Diagnostic.Error("consecutive statements must be separated by ';'");
            d.line = tree.end.line;
            d.column = tree.end.col + tree.end.stop - tree.end.start;
            d.sourceName = tree.end.source.getSourceName();
            d.context = tree.end.source.get(d.line);
            return d;
        }

        default Diagnostic InvalidAssignmentTarget(Tree tree) {
            Diagnostic d = Diagnostic.Error("invalid assignment target");
            fillRangePosition(tree, d);
            return d;
        }

        default Diagnostic InvalidFunctionName(Tree tree) {
            Diagnostic d = Diagnostic.Error("invalid function name");
            fillRangePosition(tree, d);
            return d;
        }

        private void fillRangePosition(Tree tree, Diagnostic d) {
            d.line = tree.start.line;
            d.column = tree.start.col;
            d.length = tree.end.stop - tree.start.start;
            d.sourceName = tree.start.source.getSourceName();
            d.context = tree.start.source.get(d.line);
        }

    }
}
