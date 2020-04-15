package yan.lang.predefine.abc;

import yan.foundation.driver.lang.Phase;
import yan.foundation.driver.log.Diagnostic;
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
        default Diagnostic BreakNotInLoop() {
            return new Diagnostic("BreakNotInLoop");
        }

        default Diagnostic ContinueNotInLoop() {
            return new Diagnostic("ContinueNotInLoop");
        }

        default Diagnostic ReturnNotInFunction() {
            return new Diagnostic("ReturnNotInFunction");
        }
    }
}
