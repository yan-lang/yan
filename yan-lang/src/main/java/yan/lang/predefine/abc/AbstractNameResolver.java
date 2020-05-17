package yan.lang.predefine.abc;

import yan.foundation.driver.lang.Phase;
import yan.foundation.driver.log.Diagnostic;
import yan.foundation.frontend.ast.Tree;
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

        default Diagnostic InvalidSymbol(Tree that, String expected) {
            var diagnostic = Diagnostic.Error(that.toStringTree() + " is not a " + expected);
            getDiagnostic(that, diagnostic);
            diagnostic.length = that.end.stop - that.start.start;
            return diagnostic;
        }

        default Diagnostic SymbolAlreadyDefined(String name, Tree that) {
            var diagnostic = Diagnostic.Error(name + " has been already defined");
            return getDiagnostic(that, diagnostic);
        }

        default Diagnostic MethodNotDefine(String name, Tree that) {
            var diagnostic = Diagnostic.Error("method " + name + " has not been defined.");
            return getDiagnostic(that, diagnostic);
        }

        default Diagnostic VariableNotDefine(String name, Tree that) {
            var diagnostic = Diagnostic.Error("variable " + name + " has not been defined.");
            return getDiagnostic(that, diagnostic);
        }

        private Diagnostic getDiagnostic(Tree that, Diagnostic diagnostic) {
            diagnostic.sourceName = that.start.source.getSourceName();
            diagnostic.line = that.start.line;
            diagnostic.column = that.start.col;
            diagnostic.context = that.start.source.get(diagnostic.line);
            return diagnostic;
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
