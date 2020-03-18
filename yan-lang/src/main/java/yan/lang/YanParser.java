package yan.lang;

import yan.foundation.compiler.frontend.lex.Token;
import yan.foundation.compiler.frontend.parse.ExpectationError;
import yan.foundation.driver.BaseConfig;
import yan.foundation.driver.error.Unexpected;
import yan.lang.predefine.AbstractYanParser;
import yan.lang.predefine.YanTree;

import java.util.ArrayList;
import java.util.List;

import static yan.lang.predefine.YanTree.*;

public class YanParser extends AbstractYanParser {

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
        return setRange(new Program(stmts), 0);
    }

    private Stmt parseStmt() {
        if (match(KW_VAR)) return parseVarDef();
        if (match(KW_IF)) return parseIf();
        if (match(KW_PRINT)) return parsePrint();
        if (match(LEFT_BRACE)) return parseBlock();
        if (match(NEWLINE, EOF)) return parseEmpty();
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

    private If parseIf() {
        int start = current - 1;
        consume(LEFT_PAREN);
        Expr condition = parseExpr();
        consume(RIGHT_PAREN);
        consume(LEFT_BRACE);
        Block ifBody = parseBlock();
        Block elseBody = null;
        if (match(KW_ELSE)) {
            consume(LEFT_BRACE);
            elseBody = parseBlock();
        }
        consume(NEWLINE, EOF);
        return setRange(new If(condition, ifBody, elseBody), start);
    }

    private Print parsePrint() {
        int start = current - 1;
        consume(LEFT_PAREN);
        Expr expr = parseExpr();
        consume(RIGHT_PAREN);
        consume(NEWLINE, EOF);
        return setRange(new Print(expr), start);
    }

    private Block parseBlock() {
        int start = current - 1;
        List<Stmt> stmts = new ArrayList<>();
        while (!match(RIGHT_BRACE)) {
            try {
                stmts.add(parseStmt());
            } catch (Unexpected error) {
                errorCollector.addError(error);
                recovery();
            }
        }
        return setRange(new Block(stmts), start);
    }

    private Empty parseEmpty() {
        return setRange(new Empty());
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
        Expr expr = parseEquality();
        if (match(ASSIGN)) {
            if (expr instanceof Identifier) {
                Expr value = parseExpr();
                return setRange(new Assign((Identifier) expr, value), start);
            }
//            logError(new );
        }
        return expr;
    }

    private Expr parseEquality() {
        int start = current;
        Expr left = parseComparision();
        while (check(EQUAL)) {
            Token op = consume(EQUAL);
            Expr right = parseComparision();
            left = setRange(new Binary(left, getBinaryOp(op.type), right), start);
        }
        return left;
    }

    private Expr parseComparision() {
        int start = current;
        Expr left = parseAddition();
        while (check(LARGER) || check(LESS)) {
            Token op = consume(LARGER, LESS);
            Expr right = parseAddition();
            left = setRange(new Binary(left, getBinaryOp(op.type), right), start);
        }
        return left;
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
        if (match(KW_TRUE)) return setRange(new BoolConst(true));
        if (match(KW_FALSE)) return setRange(new BoolConst(false));
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
