package yan.lang.predefine.abc;

import yan.foundation.driver.lang.Phase;
import yan.foundation.driver.log.Diagnostic;
import yan.foundation.frontend.ast.Tree;
import yan.lang.predefine.YanTree;

public abstract class AbstractControlStructureAnalyzer extends Phase<YanTree.Program, YanTree.Program> implements YanTree.VoidVisitor {

    // ------- API need to be implemented ------- //

    @Override
    abstract public void visit(YanTree.FuncDef that);

    @Override
    abstract public void visit(YanTree.Return that);

    @Override
    abstract public void visit(YanTree.While that);

    @Override
    abstract public void visit(YanTree.Continue that);

    @Override
    abstract public void visit(YanTree.Break that);

    // ------- Default Implementation ------- //

    @Override
    public void visit(YanTree.Program that) {
        that.defs.forEach(def -> def.accept(this));
    }

    @Override
    public void visit(YanTree.Block that) {
        that.stmts.forEach(stmt -> stmt.accept(this));
    }

    @Override
    public YanTree.Program transform(YanTree.Program input) {
        input.accept(this);
        return input;
    }

    // ------- Errors that might be thrown in this phase ------- //

    public static IErrors Errors = new IErrors() {
    };

    public interface IErrors {
        default Diagnostic BreakNotInLoop(YanTree.Break that) {
            return getDiagnostic(that, "break", "loop");
        }

        default Diagnostic ContinueNotInLoop(YanTree.Continue that) {
            return getDiagnostic(that, "continue", "loop");
        }

        default Diagnostic ReturnNotInFunction(YanTree.Return that) {
            return getDiagnostic(that, "return", "function");
        }

        private Diagnostic getDiagnostic(Tree that, String item, String container) {
            Diagnostic diagnostic = Diagnostic.Error(String.format("'%s' is only allowed inside a %s", item, container));
            diagnostic.sourceName = that.start.source.getSourceName();
            diagnostic.line = that.start.line;
            diagnostic.column = that.start.col;
            diagnostic.context = that.start.source.get(diagnostic.line);
            return diagnostic;
        }
    }
}
