package yan.foundation;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import yan.foundation.compiler.frontend.ast.ReflectiveVisitor;
import yan.foundation.compiler.frontend.ast.Tree;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

public class ReflectiveVisitorTest {

    static class CompilationUnit extends Tree {
        public List<Statement> statementList;

        public CompilationUnit(List<Statement> statementList) {
            this.statementList = statementList;
        }
    }

    static abstract class Statement extends Tree {

    }

    static class If extends Statement {
        public Expression cond;
        public Compound body;

        public If(Expression cond, Compound body) {
            this.cond = cond;
            this.body = body;
        }
    }

    static class Expression extends Tree {

    }

    static class Compound extends Statement {

    }

    static public class TreePrinter implements ReflectiveVisitor {
        public void visit(CompilationUnit that) {
            System.out.println("CompilationUnit");
            for (var stmt : that.statementList) {
                stmt.accept(this);
            }
        }

        public void visit(If that) {
            System.out.println("If");
            that.cond.accept(this);
            that.body.accept(this);
        }

        public void visit(Statement that) {
            System.out.println("default Statement");
        }

        public void defaultVisit(Tree tree) {
            System.out.println("Default");
        }
    }

    private static final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private static final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private static final PrintStream originalOut = System.out;
    private static final PrintStream originalErr = System.err;

    @BeforeAll
    static void beforeAll() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @AfterAll
    static void afterAll() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    @Test
    public void testVisit() {

        var compilationUnit = new CompilationUnit(List.of(new If(new Expression(), new Compound()),
                                                          new Compound()));
        var printer = new TreePrinter();
        printer.visit(compilationUnit);
        Assertions.assertEquals("CompilationUnit\n" +
                                        "If\n" +
                                        "Default\n" +
                                        "default Statement\n" +
                                        "default Statement\n", outContent.toString());
    }

}
