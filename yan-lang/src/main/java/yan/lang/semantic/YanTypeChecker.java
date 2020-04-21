package yan.lang.semantic;

import yan.foundation.compiler.frontend.semantic.v1.Type;
import yan.foundation.compiler.frontend.semantic.v1.symbol.MethodSymbol;
import yan.foundation.compiler.frontend.semantic.v1.symbol.VarSymbol;
import yan.foundation.driver.log.Diagnostic;
import yan.lang.predefine.YanTree;
import yan.lang.predefine.abc.AbstractTypeChecker;

import static yan.lang.predefine.YanTypes.Void;
import static yan.lang.predefine.YanTypes.*;

public class YanTypeChecker extends AbstractTypeChecker {

    @Override
    public void visit(YanTree.Unary that) {
        that.expr.accept(this);

        Type operandType = that.expr.evalType;

        if (!isErrorType(operandType) && !compatible(that.op.tag, operandType)) {
            logger.log(Diagnostic.Error("type " + operandType + " is incompatible for operation " + that.op.tag));
        }

        that.evalType = getResultType(that.op.tag, operandType);
    }

    @Override
    public void visit(YanTree.Binary that) {
        that.left.accept(this);
        that.right.accept(this);

        Type lhsType = that.left.evalType;
        Type rhsType = that.right.evalType;

        if (!isErrorType(lhsType) && !isErrorType(rhsType) && !compatible(that.op.tag, lhsType, rhsType)) {
            logger.log(Diagnostic.Error("type " + lhsType + " and type " + rhsType +
                                        " is incompatible for operation " + that.op.tag));
        }

        that.evalType = getResultType(that.op.tag, lhsType, rhsType);
    }


    @Override
    public void visit(YanTree.TypeCast that) {
        that.expr.accept(this);

        Type operandType = that.expr.evalType;
        Type castedType = that.evalType; // that.evalType is filled by YanNameResolver

        checkType(castedType, operandType);

        that.evalType = castedType;
    }

    @Override
    public void visit(YanTree.FunCall that) {
        // 1. 检查函数是否存在 ( 应该在NameResolver已经检查过了 )
        // 2. 检查参数数目是否匹配 ( 应该在NameResolver已经检查过了 )
        // 3. 检查参数类型是否匹配 ( 现在要检查的 )

        MethodSymbol symbol = (MethodSymbol) that.funcName.symbol; // filled by name resolver

        that.args.forEach(arg -> arg.accept(this)); // 算出所有实参的类型

        assert that.args.size() == symbol.methodType.argTypes.size();
        for (int i = 0; i < that.args.size(); i++) {
            if (!isErrorType(that.args.get(i).evalType)) {
                checkType(that.args.get(i).evalType, symbol.methodType.argTypes.get(i));
            }
        }

        that.evalType = symbol.methodType.retType;
    }

    @Override
    public void visit(YanTree.Identifier that) {
        // 表达式中的identifier包括, 函数名, 类名, 成员名, 变量名, 但只有变量名需要求类型,
        // name resolve阶段就要把所有identifier对应的symbol填好, 如果symbol类型不对需要报错
        that.evalType = ((VarSymbol) that.symbol).type;
    }

    @Override
    public void visit(YanTree.Literal that) {
        switch (that.tag) {
            case BOOL -> that.evalType = Bool;
            case INT -> that.evalType = Int;
            default -> throw new IllegalStateException("Unexpected value: " + that.tag);
        }
    }

    @Override
    public void visit(YanTree.Program that) {
        that.defs.forEach(def -> def.accept(this));
    }

    @Override
    public void visit(YanTree.VarDef that) {
        if (that.init != null) {
            that.init.accept(this);
            // check if init type compatible with type defined by user
            if (that.symbol.type != null) {
                checkType(that.symbol.type, that.init.evalType);
//                logger.log(Diagnostic.Error("bad init expression, expect " +
//                                            that.symbol.type + " got " + that.init.evalType));
            } else {
                that.symbol.type = that.init.evalType;
            }
        }
    }

    @Override
    public void visit(YanTree.ClassDef that) {
        // ignore
    }

    @Override
    public void visit(YanTree.FuncDef that) {
        that.params.forEach(param -> param.accept(this));
        that.body.accept(this);
    }

    @Override
    public void visit(YanTree.Return that) {
        if (that.expr != null) {
            if (that.func.symbol.methodType.retType == Void) {
                logger.log(Diagnostic.Error("void function should not return a value"));
            } else {
                that.expr.accept(this);
                checkType(that.func.symbol.methodType.retType, that.expr.evalType);
            }
        }
    }

    @Override
    public void visit(YanTree.If that) {
        that.condition.accept(this);
        checkType(Bool, that.condition.evalType);
        that.ifBody.accept(this);
        if (that.elseBody != null) that.elseBody.accept(this);
    }

    @Override
    public void visit(YanTree.While that) {
        that.condition.accept(this);
        checkType(Bool, that.condition.evalType);
        that.body.accept(this);
    }

    @Override
    public void visit(YanTree.ExprStmt that) {
        that.expr.accept(this);
    }

    @Override
    public void visit(YanTree.Block that) {
        that.stmts.forEach(stmt -> stmt.accept(this));
    }

}
