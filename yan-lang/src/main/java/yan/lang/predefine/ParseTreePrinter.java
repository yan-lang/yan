package yan.lang.predefine;

import yan.foundation.driver.PhaseFormatter;
import yan.foundation.utils.printer.XMLPrinter;

public class ParseTreePrinter implements YanTree.YanVisitor<Object>, PhaseFormatter<YanTree.Program> {

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
    public Object visit(YanTree.Program program) {
        printer.openElement("Program");
        program.stmts.forEach(stmt -> stmt.accept(this));
        // Note: when XMLPrinter flush its content, it will close the root element.
        //       So we should not close it here.
        // printer.closeElement();
        return null;
    }

    @Override
    public Object visit(YanTree.VarDef varDef) {
        printer.openElement("VarDef");
        varDef.identifier.accept(this);
        varDef.initializer.accept(this);
        printer.closeElement();
        return null;
    }

    @Override
    public Object visit(YanTree.ExprStmt exprStmt) {
        printer.openElement("ExprStmt");
        exprStmt.expr.accept(this);
        printer.closeElement();
        return null;
    }

    @Override
    public Object visit(YanTree.Assign assign) {
        printer.openElement("Assign");
        assign.identifier.accept(this);
        assign.expr.accept(this);
        printer.closeElement();
        return null;
    }

    @Override
    public Object visit(YanTree.Binary binary) {
        printer.openElement("Binary");
        printer.pushAttribute("type", binary.op.toString());
        binary.left.accept(this);
        binary.right.accept(this);
        printer.closeElement();
        return null;
    }

    @Override
    public Object visit(YanTree.Identifier identifier) {
        printer.pushSimpleElement("Identifier", identifier.name);
        return null;
    }

    @Override
    public Object visit(YanTree.IntConst intConst) {
        printer.pushSimpleElement("IntConst", String.valueOf(intConst.value));
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
