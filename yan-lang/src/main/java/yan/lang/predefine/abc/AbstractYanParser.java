package yan.lang.predefine.abc;

import yan.foundation.compiler.frontend.lex.Token;
import yan.foundation.compiler.frontend.parse.AbstractParser;
import yan.foundation.compiler.frontend.parse.BaseParserDiagnostic;
import yan.foundation.driver.log.Diagnostic;
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
            case REL_OR -> YanTree.Operator.Tag.REL_OR;
            case REL_AND -> YanTree.Operator.Tag.REL_AND;
            case REL_NOT -> YanTree.Operator.Tag.REL_NOT;
            default -> throw new IllegalStateException(token.getText() + " is not a operator token.");
        };
    }

    @Override
    protected TokenTypeStringMapper getTokenTypeStringMapper() { return tokenSymbolNames::get; }

    // ------- Errors that might be thrown in this phase ------- //

    public static IErrors Errors = new IErrors() {
    };

    public interface IErrors extends BaseParserDiagnostic.IErrors {
        default Diagnostic TopLevelStatement(YanTree tree) {
            return new Diagnostic("TopLevelStatement");
        }

        default Diagnostic UnknownTopLevelDefinition() {
            return new Diagnostic("UnknownTopLevelDefinition");

        }

        default Diagnostic InvalidAssignmentTarget() {
            return new Diagnostic("InvalidAssignmentTarget");
        }

        default Diagnostic ConsecutiveStatements() {
            return new Diagnostic("ConsecutiveStatements");
        }

        default Diagnostic InvalidFunctionName() {
            return new Diagnostic("InvalidFunctionName");
        }
    }
}
