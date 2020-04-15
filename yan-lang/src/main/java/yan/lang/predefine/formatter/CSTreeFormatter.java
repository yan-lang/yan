package yan.lang.predefine.formatter;

import yan.foundation.driver.lang.Formatter;
import yan.lang.predefine.YanTree;

/**
 * Tree formatter for Control Structure Analyzer
 */
public class CSTreeFormatter implements YanTree.VoidVisitor, Formatter<YanTree.Program> {
    StringBuilder builder;
    final String template = "line %d: %s found, %s\n";
    final String detailTemplate = "attached %s at line %d";

    @Override
    public String format(YanTree.Program program) {
        builder = new StringBuilder();
        program.accept(this);
        return builder.toString();
    }

    @Override
    public void visit(YanTree.Return that) {
        String detail = that.func == null ? "invalid" : String.format(detailTemplate, "func", 0);
        builder.append(String.format(template, 0, "return", detail));
    }

    @Override
    public void visit(YanTree.Continue that) {
        String detail = that.attachedLoop == null ? "invalid" : String.format(detailTemplate, "loop", 0);
        builder.append(String.format(template, 0, "continue", detail));
    }

    @Override
    public void visit(YanTree.Break that) {
        String detail = that.attachedLoop == null ? "invalid" : String.format(detailTemplate, "loop", 0);
        builder.append(String.format(template, 0, "break", detail));
    }

    @Override
    public void visit(YanTree.Program that) {
        that.defs.forEach(def -> def.accept(this));
    }

    @Override
    public void visit(YanTree.ClassDef that) {
        // class is not supported for now
    }

    @Override
    public void visit(YanTree.FuncDef that) {
        that.body.accept(this);
    }

    @Override
    public void visit(YanTree.While that) {
        that.body.accept(this);
    }

    @Override
    public void visit(YanTree.If that) {
        that.ifBody.accept(this);
        if (that.elseBody != null) that.elseBody.accept(this);
    }

    @Override
    public void visit(YanTree.Block that) {
        that.stmts.forEach(stmt -> stmt.accept(this));
    }

}
