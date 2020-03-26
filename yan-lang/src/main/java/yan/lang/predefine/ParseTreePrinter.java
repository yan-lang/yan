package yan.lang.predefine;

import yan.foundation.compiler.frontend.ast.Tree;
import yan.foundation.driver.PhaseFormatter;
import yan.foundation.utils.printer.XMLPrinter;

public class ParseTreePrinter implements YanTree.Visitor, PhaseFormatter<YanTree.Program> {

    private XMLPrinter printer = new XMLPrinter();

    public String print(YanTree.Program program) {
        program.accept(this);
        return flush();
    }

    public String flush() {
        String result = printer.flush();
        printer = new XMLPrinter();
        return result;
    }

    @Override
    public void visit(YanTree.Program program) {
        printer.openElement("Program");
        printer.pushAttribute("from", String.valueOf(program.range.from));
        printer.pushAttribute("to", String.valueOf(program.range.to));

        program.stmts.forEach(stmt -> stmt.accept(this));

        // Note: when XMLPrinter flush its content, it will close the root element.
        //       So we should not close it here.
        // printer.closeElement();
    }

    @Override
    public void visit(YanTree.VarDef varDef) {
        print(varDef, () -> {
            varDef.identifier.accept(this);
            varDef.initializer.accept(this);
        });
    }

    @Override
    public void visit(YanTree.ExprStmt exprStmt) {
        print(exprStmt, () -> exprStmt.expr.accept(this));
    }

    @Override
    public void visit(YanTree.Assign assign) {
        print(assign, () -> {
            assign.identifier.accept(this);
            assign.expr.accept(this);
        });
    }

    @Override
    public void visit(YanTree.Binary binary) {
        print(binary, () -> {
            printer.pushAttribute("type", binary.op.toString());
            binary.left.accept(this);
            binary.right.accept(this);
        });
    }

    @Override
    public void visit(YanTree.Identifier identifier) {
        print(identifier, true, () -> printer.pushText(identifier.name));
    }

    @Override
    public void visit(YanTree.IntConst intConst) {
        print(intConst, true, () -> printer.pushText(String.valueOf(intConst.value)));
    }

    @Override
    public void visit(YanTree.If ifStmt) {
        print(ifStmt, () -> {
            ifStmt.condition.accept(this);
            ifStmt.ifBody.accept(this);
            if (ifStmt.elseBody != null) ifStmt.elseBody.accept(this);
        });
    }

    @Override
    public void visit(YanTree.Block block) {
        print(block, () -> {
            for (var stmt : block.stmts) {
                stmt.accept(this);
            }
        });
    }

    @Override
    public void visit(YanTree.Print print) {
        print(print, () -> print.expr.accept(this));
    }

    @Override
    public void visit(YanTree.Empty empty) {
        print(empty, true, null);
    }

    @Override
    public void visit(YanTree.BoolConst boolConst) {
        print(boolConst, true, () -> printer.pushText(String.valueOf(boolConst.value)));
    }

    private interface DetailPrinter {
        void print();
    }

    private <T extends Tree> void print(T node, DetailPrinter detailPrinter) {
        print(node, false, detailPrinter);
    }

    private <T extends Tree> void print(T node, boolean compactMode, DetailPrinter detailPrinter) {
        printer.openElement(node.getClass().getSimpleName(), compactMode);
        printer.pushAttribute("from", String.valueOf(node.range.from));
        printer.pushAttribute("to", String.valueOf(node.range.to));
        if (detailPrinter != null) detailPrinter.print();
        printer.closeElement();
    }

    @Override
    public String toString(YanTree.Program program) { return print(program); }

    @Override
    public String fileExtension() { return "xml"; }

    @Override
    public String targetName() { return "parse"; }
}
