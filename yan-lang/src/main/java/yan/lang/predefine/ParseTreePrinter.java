package yan.lang.predefine;

import yan.foundation.compiler.frontend.ast.Tree;
import yan.foundation.driver.lang.Formatter;
import yan.foundation.utils.printer.XMLPrinter;

public class ParseTreePrinter implements YanTree.Visitor, Formatter<YanTree.Program> {

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

        program.defs.forEach(def -> def.accept(this));

        // Note: when XMLPrinter flush its content, it will close the root element.
        //       So we should not close it here.
        // printer.closeElement();
    }

    @Override
    public void visit(YanTree.ClassDef that) {

    }

    @Override
    public void visit(YanTree.FuncDef that) {
        print(that, () -> {
            that.id.accept(this);
//            that.retType.accept(this);
            that.params.forEach(param -> param.accept(this));
            that.body.accept(this);
        });
    }

    @Override
    public void visit(YanTree.While that) {
        print(that, () -> {
            that.condition.accept(this);
            that.body.accept(this);
        });
    }

    @Override
    public void visit(YanTree.Continue that) {
        print(that, true, () -> {});
    }

    @Override
    public void visit(YanTree.Break that) {
        print(that, true, () -> {});
    }

    @Override
    public void visit(YanTree.Operator that) {
        print(that, true, () -> printer.pushText(that.tag.toString().toLowerCase()));
    }

    @Override
    public void visit(YanTree.TypeCast that) {

    }

    @Override
    public void visit(YanTree.VarDef varDef) {
        print(varDef, () -> {
            varDef.id.accept(this);
            varDef.init.accept(this);
        });
    }

    @Override
    public void visit(YanTree.ExprStmt exprStmt) {
        print(exprStmt, () -> exprStmt.expr.accept(this));
    }

    @Override
    public void visit(YanTree.Unary unary) {
        print(unary, () -> {
            printer.pushAttribute("type", unary.op.tag.toString().toLowerCase());
            unary.expr.accept(this);
        });
    }

    @Override
    public void visit(YanTree.Binary binary) {
        print(binary, () -> {
            printer.pushAttribute("type", binary.op.tag.toString().toLowerCase());
            binary.left.accept(this);
            binary.right.accept(this);
        });
    }

    @Override
    public void visit(YanTree.Identifier identifier) {
        print(identifier, true, () -> printer.pushText(identifier.name));
    }

    @Override
    public void visit(YanTree.Literal literal) {
        print(literal, true, () -> printer.pushText(String.valueOf(literal.value)));
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
    public String format(YanTree.Program program) {
        return print(program);
    }
}
