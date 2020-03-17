package yan.lang.predefine;

import yan.foundation.compiler.frontend.ast.TreeNode;
import yan.foundation.driver.PhaseFormatter;
import yan.foundation.utils.printer.XMLPrinter;

public class ParseTreePrinter implements YanTree.YanVisitor<Void>, PhaseFormatter<YanTree.Program> {

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
    public Void visit(YanTree.Program program) {
        printer.openElement("Program");
        printer.pushAttribute("from", String.valueOf(program.range.from));
        printer.pushAttribute("to", String.valueOf(program.range.to));

        program.stmts.forEach(stmt -> stmt.accept(this));

        // Note: when XMLPrinter flush its content, it will close the root element.
        //       So we should not close it here.
        // printer.closeElement();
        return null;
    }

    @Override
    public Void visit(YanTree.VarDef varDef) {
        return print(varDef, () -> {
            varDef.identifier.accept(this);
            varDef.initializer.accept(this);
        });
    }

    @Override
    public Void visit(YanTree.ExprStmt exprStmt) {
        return print(exprStmt, () -> exprStmt.expr.accept(this));
    }

    @Override
    public Void visit(YanTree.Assign assign) {
        return print(assign, () -> {
            assign.identifier.accept(this);
            assign.expr.accept(this);
        });
    }

    @Override
    public Void visit(YanTree.Binary binary) {
        return print(binary, () -> {
            printer.pushAttribute("type", binary.op.toString());
            binary.left.accept(this);
            binary.right.accept(this);
        });
    }

    @Override
    public Void visit(YanTree.Identifier identifier) {
        return print(identifier, true, () -> printer.pushText(identifier.name));
    }

    @Override
    public Void visit(YanTree.IntConst intConst) {
        return print(intConst, true, () -> printer.pushText(String.valueOf(intConst.value)));
    }

    private interface DetailPrinter {
        void print();
    }

    private <T extends TreeNode> Void print(T node, DetailPrinter detailPrinter) {
        return print(node, false, detailPrinter);
    }

    private <T extends TreeNode> Void print(T node, boolean compactMode, DetailPrinter detailPrinter) {
        printer.openElement(node.getClass().getSimpleName(), compactMode);
        printer.pushAttribute("from", String.valueOf(node.range.from));
        printer.pushAttribute("to", String.valueOf(node.range.to));
        detailPrinter.print();
        printer.closeElement();
        return null;
    }

    @Override
    public String toString(YanTree.Program program) {
        return print(program);
    }

    @Override
    public String fileExtension() {
        return "xml";
    }

    @Override
    public String targetName() {
        return "parse";
    }
}
