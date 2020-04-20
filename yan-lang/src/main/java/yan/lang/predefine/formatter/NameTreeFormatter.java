package yan.lang.predefine.formatter;

import yan.foundation.compiler.frontend.semantic.v1.symbol.ClassSymbol;
import yan.foundation.driver.lang.Formatter;
import yan.lang.predefine.YanTree;

public class NameTreeFormatter implements YanTree.VoidVisitor, Formatter<YanTree.Program> {

    StringBuilder builder = new StringBuilder();

    @Override
    public String format(YanTree.Program program) {
        builder = new StringBuilder();
        program.accept(this);
        return builder.toString();
    }

    @Override
    public void visit(YanTree.Program that) {
        that.defs.forEach(def -> def.accept(this));
    }

    @Override
    public void visit(YanTree.VarDef that) {
        builder.append(String.format("def var '%s' at line %d, type: %s\n",
                                     that.id.name, that.start.line, that.symbol.type));
        if (that.init != null) that.init.accept(this);
    }

    @Override
    public void visit(YanTree.ClassDef that) {
        builder.append(String.format("def class '%s' at line %d, super class: %s\n",
                                     that.id.name, that.start.line,
                                     that.superClass == null ?
                                             "null" : ((ClassSymbol) that.superClass.symbol).classType));
        that.defs.forEach(def -> def.accept(this));
    }

    @Override
    public void visit(YanTree.FuncDef that) {
        builder.append(String.format("def func '%s' at line %d\n", that.id.name, that.start.line));
        that.params.forEach(param -> param.accept(this));
        that.body.accept(this);
    }

    @Override
    public void visit(YanTree.Return that) {
        that.expr.accept(this);
    }

    @Override
    public void visit(YanTree.If that) {
        that.condition.accept(this);
        that.ifBody.accept(this);
        that.elseBody.accept(this);
    }

    @Override
    public void visit(YanTree.While that) {
        that.condition.accept(this);
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

    @Override
    public void visit(YanTree.Unary that) {
        that.expr.accept(this);
    }

    @Override
    public void visit(YanTree.Binary that) {
        that.left.accept(this);
        that.right.accept(this);
    }

    @Override
    public void visit(YanTree.TypeCast that) {
//        builder.append(String.format("ref type '%s' at line %d",
//                                     that.castedType.symbol.name, that.castedType.symbol.tree.start.line));
        that.expr.accept(this);
    }

    @Override
    public void visit(YanTree.FunCall that) {
        builder.append(String.format("ref func '%s' at line %d\n",
                                     that.funcName.symbol.name, that.funcName.symbol.tree.start.line));
        that.args.forEach(arg -> arg.accept(this));
    }

    @Override
    public void visit(YanTree.Identifier that) {
        builder.append(String.format("ref var '%s' at line %d\n",
                                     that.symbol.name, that.symbol.tree.start.line));
    }
}
