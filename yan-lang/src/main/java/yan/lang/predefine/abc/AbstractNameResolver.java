package yan.lang.predefine.abc;

import yan.foundation.driver.lang.Phase;
import yan.foundation.driver.log.Diagnostic;
import yan.lang.predefine.YanTree;

public abstract class AbstractNameResolver extends Phase<YanTree.Program, YanTree.Program> implements YanTree.VoidVisitor {

    @Override
    abstract public void visit(YanTree.Program program);

    @Override
    abstract public void visit(YanTree.VarDef varDef);

    @Override
    abstract public void visit(YanTree.ClassDef that);

    @Override
    abstract public void visit(YanTree.FuncDef that);

    @Override
    abstract public void visit(YanTree.Block block);

    @Override
    abstract public void visit(YanTree.TypeCast that);

    @Override
    abstract public void visit(YanTree.FunCall that);

    @Override
    abstract public void visit(YanTree.Identifier identifier);

    // ------- Errors that might be thrown in this phase ------- //

    public static IErrors Errors = new IErrors() {
    };

    public interface IErrors {

        default Diagnostic VariableNotDefine() {
            return Diagnostic.Error("VariableNotDefine");
        }

        default Diagnostic FunctionNotDefine() {
            return Diagnostic.Error("FunctionNotDefine");
        }

        // expect function, but got variable

        default Diagnostic InvalidSymbol() {
            return Diagnostic.Error("InvalidSymbol");
        }

        default Diagnostic SymbolAlreadyDefined() {
            return Diagnostic.Error("SymbolAlreadyDefined");
        }

        default Diagnostic typeNotFound() {
            return Diagnostic.Error("typeNotFound");
        }

        default Diagnostic FunctionAlreadyDefined() {
            return Diagnostic.Error("FunctionAlreadyDefined");
        }

        default Diagnostic MethodNotDefine() {
            return Diagnostic.Error("MethodNotDefine");
        }

        default Diagnostic FieldNotDefine() {
            return Diagnostic.Error("FieldNotDefine");
        }
    }

    @Override
    public YanTree.Program transform(YanTree.Program input) {
        input.accept(this);
        return input;
    }

    @Override
    public void visit(YanTree.Return that) {
        that.expr.accept(this);
    }

    @Override
    public void visit(YanTree.If ifStmt) {
        ifStmt.condition.accept(this);
        ifStmt.ifBody.accept(this);
        if (ifStmt.elseBody != null) ifStmt.elseBody.accept(this);
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
    public void visit(YanTree.Unary that) {
        that.expr.accept(this);
    }

    @Override
    public void visit(YanTree.Binary that) {
        that.right.accept(this);
        that.left.accept(this);
    }
}
