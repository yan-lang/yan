package yan.lang.predefine.formatter;

import yan.foundation.driver.lang.Formatter;
import yan.foundation.frontend.ast.Tree;
import yan.foundation.utils.printer.XMLPrinter;
import yan.lang.predefine.YanTree;

public class TypeTreeFormatter implements YanTree.VoidVisitor, Formatter<YanTree.Program> {
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
        printer.pushAttribute("from", String.valueOf(program.range().from));
        printer.pushAttribute("to", String.valueOf(program.range().to));

        program.defs.forEach(def -> def.accept(this));

        // Note: when XMLPrinter flush its content, it will close the root element.
        //       So we should not close it here.
        // printer.closeElement();
    }

    @Override
    public void visit(YanTree.ClassDef that) {
        print(that, () -> {
            printer.pushSimpleElement("Name", that.id.name);
            printer.pushSimpleElement("SuperClass", that.superClass == null ? "null" : that.superClass.name);
            printer.openElement("Members");
            that.defs.forEach(def -> def.accept(this));
            printer.closeElement();
        });
    }

    @Override
    public void visit(YanTree.FuncDef that) {
        print(that, () -> {
            that.id.accept(this);
            printer.pushSimpleElement("ReturnType", that.id == null ? "void" : that.id.name);
            printer.openElement("Params");
            that.params.forEach(param -> param.accept(this));
            printer.closeElement();
            that.body.accept(this);
        });
    }

    @Override
    public void visit(YanTree.Return that) {
        print(that, () -> {
            printer.openElement("Value");
            if (that.expr != null) that.expr.accept(this);
            printer.closeElement();
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
        print(that, () -> {
            that.castedType.accept(this);
            that.expr.accept(this);
        });
    }

    @Override
    public void visit(YanTree.VarDef varDef) {
        print(varDef, () -> {
            printer.pushSimpleElement("Name", varDef.id.name);
            printer.pushSimpleElement("Type", varDef.symbol.type.toString());
            printer.openElement("Init");
            if (varDef.init != null) varDef.init.accept(this);
            printer.closeElement();
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
            printer.pushAttribute("evaltype", unary.evalType.toString());
            unary.expr.accept(this);
        });
    }

    @Override
    public void visit(YanTree.Binary binary) {
        print(binary, () -> {
            printer.pushAttribute("type", binary.op.tag.toString().toLowerCase());
            printer.pushAttribute("evaltype", binary.evalType.toString());
            binary.left.accept(this);
            binary.right.accept(this);
        });
    }

    @Override
    public void visit(YanTree.FunCall that) {
        print(that, () -> {
            printer.pushAttribute("evaltype", that.evalType.toString());
            printer.pushSimpleElement("FuncName", that.funcName.name);
            printer.openElement("Args");
            that.args.forEach(arg -> arg.accept(this));
            printer.closeElement();
        });
    }

    @Override
    public void visit(YanTree.Identifier identifier) {
        print(identifier, true, () -> {
            printer.pushAttribute("evaltype",
                                  identifier.evalType == null ? "null" : identifier.evalType.toString());
            printer.pushText(identifier.name);
        });
    }

    @Override
    public void visit(YanTree.Literal literal) {
        print(literal, true, () -> {
            printer.pushAttribute("evaltype", literal.evalType.toString());
            printer.pushText(String.valueOf(literal.value));
        });
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
    public void visit(YanTree.Empty empty) {
//        print(empty, true, null);
    }

    private interface DetailPrinter {
        void print();
    }

    private <T extends Tree> void print(T node, DetailPrinter detailPrinter) {
        print(node, false, detailPrinter);
    }

    private <T extends Tree> void print(T node, boolean compactMode, DetailPrinter detailPrinter) {
        printer.openElement(node.getClass().getSimpleName(), compactMode);
        printer.pushAttribute("from", String.valueOf(node.range().from));
        printer.pushAttribute("to", String.valueOf(node.range().to));
        if (detailPrinter != null) detailPrinter.print();
        printer.closeElement();
    }

    @Override
    public String format(YanTree.Program program) {
        return print(program);
    }
}
