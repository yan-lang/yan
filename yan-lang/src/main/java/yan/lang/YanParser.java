package yan.lang;

import yan.foundation.compiler.frontend.lex.Token;
import yan.foundation.compiler.frontend.parse.ExpectationError;
import yan.foundation.driver.BaseConfig;
import yan.foundation.driver.error.Unexpected;
import yan.lang.predefine.BaseYanParser;
import yan.lang.predefine.YanTree;

import java.util.ArrayList;
import java.util.List;

import static yan.lang.predefine.YanTree.*;

public class YanParser extends BaseYanParser {

    public YanParser(String name, BaseConfig config) {
        super(name, config);
    }

    @Override
    public Program parse() {
        List<YanTree.Stmt> stmts = new ArrayList<>();
        while (!isAtEnd()) {
            try {
                stmts.add(parseStmt());
            } catch (Unexpected error) {
                errorCollector.addError(error);
                recovery();
            }
        }
        return new Program(stmts);
    }

    private Stmt parseStmt() {
        if (match(KW_VAR)) return parseVarDef();
        return parseExprStmt();
    }

    private VarDef parseVarDef() {
        int start = current - 1;
        Identifier id = parseIdentifier();
        consume(ASSIGN);
        Expr init = parseExpr();
        consume(NEWLINE, EOF);
        return setRange(new VarDef(id, init), start);
    }

    private ExprStmt parseExprStmt() {
        int start = current;
        Expr expr = parseExpr();
        consume(NEWLINE, EOF);
        return setRange(new ExprStmt(expr), start);
    }

    private Expr parseExpr() {
        return parseAssign();
    }

    private Expr parseAssign() {
        int start = current;
        Expr expr = parseAddition();
        if (match(ASSIGN)) {
            if (expr instanceof Identifier) {
                Expr value = parseExpr();
                return setRange(new Assign((Identifier) expr, value), start);
            }
//            logError(new );
        }
        return expr;
    }

    private BinaryOp getBinaryOp(int tokenType) {
        switch (tokenType) {
            case PLUS:
                return BinaryOp.PLUS;
            case MINUS:
                return BinaryOp.MINUS;
            case MULTI:
                return BinaryOp.MULTI;
            case DIV:
                return BinaryOp.DIV;
            case EXP:
                return BinaryOp.EXP;
        }
        throw new RuntimeException(String.format("Invalid token type {%d} for binary operator.", tokenType));
    }

    private Expr parseAddition() {
        int start = current;
        Expr left = parseMultiplication();
        while (check(PLUS) || check(MINUS)) {
            Token op = consume(PLUS, MINUS);
            Expr right = parseMultiplication();
            left = setRange(new Binary(left, getBinaryOp(op.type), right), start);
        }
        return left;
    }

    private Expr parseMultiplication() {
        int start = current;
        Expr left = parsePrimary();
        while (check(MULTI) || check(DIV)) {
            Token op = consume(MULTI, DIV);
            Expr right = parsePrimary();
            left = setRange(new Binary(left, getBinaryOp(op.type), right), start);
        }
        return left;
    }

    private Expr parsePrimary() {
        if (match(INT_CONST)) return setRange(new IntConst(previous().getIntValue()));
        if (match(IDENTIFIER)) return setRange(new Identifier(previous().getStrValue()));
        throw new ExpectationError("expression", previous(), ExpectationError.AFTER);
    }

    private Identifier parseIdentifier() {
        Token id = consume(IDENTIFIER);
        return setRange(new Identifier(id.getStrValue()));
    }

    protected void logError(Unexpected error) {
        errorCollector.addError(error);
    }
}
