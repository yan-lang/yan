package yan.lang;

import yan.foundation.compiler.frontend.lex.Token;
import yan.foundation.driver.log.Diagnostic;
import yan.lang.predefine.YanTree;
import yan.lang.predefine.abc.AbstractYanParser;

import java.util.ArrayList;
import java.util.List;

import static yan.lang.predefine.YanTree.*;

public class YanParser extends AbstractYanParser {

    @Override
    public Program parse() {
        List<YanTree> defs = new ArrayList<>();
        while (!isAtEnd()) {
            try {
                if (defs.size() > 0 && !(defs.get(defs.size() - 1) instanceof Empty))
                    consume(() -> Errors.ConsecutiveStatements(defs.get(defs.size() - 1)),
                            NEWLINE, SEMICOLON, EOF);
                if (isAtEnd()) break; // handle statements like -> var a = 10\n
                defs.add(parseDefs());
            } catch (Diagnostic diagnostic) {
                logger.log(diagnostic);
                recovery();
            }
        }
        return setRange(new Program(defs), 0);
    }

    /**
     * Parse class, function, global variable definition
     *
     * @return defs
     */
    YanTree parseDefs() {
        if (check(KW_VAR)) return parseVarDef();
        if (check(KW_CLASS)) return parseClassDef();
        if (check(KW_FUNC)) return parseFuncDef();

        return parseStmt();
    }

    YanTree parseClassDef() {
        int start = current;
        consume(KW_CLASS);
        Identifier id = parseIdentifier();
        Identifier superClass = match(COLON) ? parseIdentifier() : null;
        List<YanTree> defs = parseClassBody();
        return setRange(new ClassDef(id, superClass, defs), start);
    }

    List<YanTree> parseClassBody() {
        consume(LEFT_BRACE);
        List<YanTree> defs = new ArrayList<>();
        while (!check(RIGHT_BRACE, EOF)) {
            defs.addAll(parseClassDeclaration());
        }
        consume(RIGHT_BRACE);
        return defs;
    }

    List<YanTree> parseClassDeclaration() {
        if (match(NEWLINE)) return List.of();
        if (check(KW_VAR)) return List.of(parseVarDef());
        if (check(KW_FUNC)) return List.of(parseFuncDef());
        throw Diagnostic.Error("only variable and function are allowed in class body");
    }

    YanTree parseFuncDef() {
        int start = current;
        consume(KW_FUNC);
        Identifier id = parseIdentifier();
        List<VarDef> params = parseParameters();
        Identifier retType = null;
        if (match(ARROW)) { retType = parseType(); }
        Block body = parseBlock();
        return setRange(new FuncDef(id, retType, params, body), start);
    }

    Identifier parseType() {
        return parseIdentifier();
    }

    List<VarDef> parseParameters() {
        consume(LEFT_PAREN);
        List<VarDef> params = new ArrayList<>();
        if (!check(RIGHT_PAREN)) {
            do {
                params.add(parseParameter());
            } while (match(COMMA));
        }
        consume(RIGHT_PAREN);
        return params;
    }

    VarDef parseParameter() {
        int start = current;
        Identifier id = parseIdentifier();
        consume(COLON);
        Identifier type = parseType();
        return setRange(new VarDef(id, null, type), start);
    }

    Block parseBlock() {
        int start = current;
        consume(LEFT_BRACE);
        List<Stmt> stmts = new ArrayList<>();
        while (!isAtEnd() && !match(RIGHT_BRACE)) {
            try {
                // 一行只能有一个statement, 除非它们之间有分号分隔
                if (stmts.size() > 0 && !(stmts.get(stmts.size() - 1) instanceof Empty))
                    consume(() -> Errors.ConsecutiveStatements(stmts.get(stmts.size() - 1)),
                            NEWLINE, SEMICOLON);
                if (match(RIGHT_BRACE)) break;
                stmts.add(parseStmt());
            } catch (Diagnostic diagnostic) {
                logger.log(diagnostic);
                recovery();
                // recovery之后有可能把这个Block的右括号消耗掉
                if (previous().type == RIGHT_BRACE) break;
            }
        }
        return setRange(new Block(stmts), start);
    }

    Stmt parseStmt() {
        if (check(KW_VAR)) return parseVarDef();
        if (check(KW_IF)) return parseIf();
        if (check(KW_WHILE)) return parseWhile();
        if (check(KW_BREAK)) return parseBreak();
        if (check(KW_CONTINUE)) return parseContinue();
        if (check(KW_RETURN)) return parseReturn();
        if (check(LEFT_BRACE)) return parseBlock();
        if (check(NEWLINE, SEMICOLON, EOF)) return parseEmpty();
        return parseExprStmt();
    }

    Return parseReturn() {
        int start = current;
        consume(KW_RETURN);
        if (check(NEWLINE, EOF)) { return setRange(new Return(null), start); }
        Expr expr = parseExpr();
        return setRange(new Return(expr), start);
    }

    /**
     * 'var' id [':' type] '=' expr
     */
    VarDef parseVarDef() {
        int start = current;
        consume(KW_VAR);
        Identifier id = parseIdentifier();
        Identifier type = match(COLON) ? parseType() : null;
        Expr init = match(ASSIGN) ? parseExpr() : null;
        return setRange(new VarDef(id, init, type), start);
    }

    Expr parseCondition() {
        consume(LEFT_PAREN);
        Expr condition = parseExpr();
        consume(RIGHT_PAREN);
        return condition;
    }

    If parseIf() {
        int start = current;
        consume(KW_IF);
        Expr condition = parseCondition();
        Block ifBody = parseBlock();
        Block elseBody = match(KW_ELSE) ? parseBlock() : null;
        return setRange(new If(condition, ifBody, elseBody), start);
    }

    While parseWhile() {
        int start = current;
        consume(KW_WHILE);
        Expr condition = parseCondition();
        Block body = parseBlock();
        return setRange(new While(condition, body), start);
    }

    Continue parseContinue() {
        int start = current;
        consume(KW_CONTINUE);
        return setRange(new Continue(), start);
    }

    Break parseBreak() {
        int start = current;
        consume(KW_BREAK);
        return setRange(new Break(), start);
    }

    Empty parseEmpty() {
        int start = current;
        consume(EOF, NEWLINE);
        return setRange(new Empty(), start);
    }

    ExprStmt parseExprStmt() {
        int start = current;
        Expr expr = parseExpr();
        return setRange(new ExprStmt(expr), start);
    }

    Expr parseExpr() {
        return parseAssign();
    }

    Expr parseAssign() {
        int start = current;
        Expr expr = parseLogicalOr();
        if (match(ASSIGN)) {
            Expr value = parseExpr();
            if (expr instanceof Identifier) {
                return setRange(new Binary(setRange(new Operator(Operator.Tag.ASSIGN)), expr, value), start);
            }
            logger.log(Errors.InvalidAssignmentTarget(expr));
        }
        return expr;
    }

    Expr parseLogicalOr() {
        int start = current;
        Expr left = parseLogicalAnd();
        while (check(REL_AND)) {
            Token op = consume(REL_AND);
            Expr right = parseLogicalAnd();
            left = setRange(new Binary(setRange(new Operator(getOperatorTag(op))), left, right), start);
        }
        return left;
    }

    Expr parseLogicalAnd() {
        int start = current;
        Expr left = parseEquality();
        while (check(REL_OR)) {
            Token op = consume(REL_OR);
            Expr right = parseEquality();
            left = setRange(new Binary(setRange(new Operator(getOperatorTag(op))), left, right), start);
        }
        return left;
    }

    Expr parseEquality() {
        int start = current;
        Expr left = parseComparision();
        while (check(EQ, NEQ)) {
            Token op = consume(EQ, NEQ);
            Expr right = parseComparision();
            left = setRange(new Binary(setRange(new Operator(getOperatorTag(op))), left, right), start);
        }
        return left;
    }

    Expr parseComparision() {
        int start = current;
        Expr left = parseAddition();
        while (check(GT, GTE, LT, LTE)) {
            Token op = consume(GT, GTE, LT, LTE);
            Expr right = parseAddition();
            left = setRange(new Binary(setRange(new Operator(getOperatorTag(op))), left, right), start);
        }
        return left;
    }

    Expr parseAddition() {
        int start = current;
        Expr left = parseMultiplication();
        while (check(PLUS) || check(MINUS)) {
            Token op = consume(PLUS, MINUS);
            Expr right = parseMultiplication();
            left = setRange(new Binary(setRange(new Operator(getOperatorTag(op))), left, right), start);
        }
        return left;
    }

    Expr parseMultiplication() {
        int start = current;
        Expr left = parseTypeCast();
        while (check(MULTI) || check(DIV)) {
            Token op = consume(MULTI, DIV);
            Expr right = parseTypeCast();
            left = setRange(new Binary(setRange(new Operator(getOperatorTag(op))), left, right), start);
        }
        return left;
    }

    Expr parseTypeCast() {
        if (match(LEFT_PAREN) && check(IDENTIFIER)) {
            Identifier type = parseType();
            consume(RIGHT_PAREN);
            Expr expr = parseUnary();
            return new YanTree.TypeCast(type, expr);
        }
        return parseUnary();
    }

    Expr parseUnary() {
        int start = current;
        if (check(MINUS) || check(REL_NOT)) {
            Token op = consume(MINUS, REL_NOT);
            Expr right = parsePostfix();
            return setRange(new Unary(setRange(new Operator(getOperatorTag(op))), right), start);
        }
        return parsePostfix();
    }

    Expr parsePostfix() {
        int start = current;
        Expr expr = parsePrimary();
        if (match(LEFT_PAREN)) {
            if (!(expr instanceof Identifier)) logger.log(Errors.InvalidFunctionName(expr));
            List<Expr> args = new ArrayList<>();
            if (!match(RIGHT_PAREN)) {
                do {
                    args.add(parseExpr());
                } while (match(COMMA));
                consume(RIGHT_PAREN);
            }
            Identifier name = (expr instanceof Identifier) ? (Identifier) expr : null;
            return setRange(new FunCall(name, args), start);
        }
        return expr;
    }

    Expr parsePrimary() {
        if (match(INT_CONST)) return setRange(new Literal(Literal.Tag.INT, previous().getIntValue()));
        if (match(KW_TRUE)) return setRange(new Literal(Literal.Tag.BOOL, true));
        if (match(KW_FALSE)) return setRange(new Literal(Literal.Tag.BOOL, false));
        if (match(IDENTIFIER)) return setRange(new Identifier(previous().getStringValue()));
        if (match(LEFT_PAREN)) {
            Expr expr = parseExpr();
            consume(RIGHT_PAREN);
            return expr;
        }
        // TODO(4-12): 照理说anchor应该是前一个Token，但是这样的话，当出错的是第一个Token的时候，previous会出错。
        throw Errors.ExpectationError("expression", previous(), "after");
    }

    Identifier parseIdentifier() {
        Token id = consume(IDENTIFIER);
        return setRange(new Identifier(id.getStringValue()));
    }

}
