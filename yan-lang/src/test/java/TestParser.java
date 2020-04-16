import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import yan.foundation.compiler.frontend.ast.Range;
import yan.foundation.compiler.frontend.parse.Parser;
import yan.foundation.driver.lang.Code;
import yan.foundation.driver.lang.Phase;
import yan.lang.YanLexer;
import yan.lang.YanParser;
import yan.lang.predefine.YanTree;

public class TestParser {
    @Test
    public void testRange() {
        YanLexer lexer = new YanLexer();
        Parser<YanTree.Program> parser = new YanParser();
        var tokens = lexer.transform(Code.of("test", "var a = 10 * b\nb = 10*9"));
        YanTree.Program out = parser.parse(tokens);

        YanTree.VarDef def0 = (YanTree.VarDef) out.defs.get(0);
        Assertions.assertEquals(new Range(1, 1), def0.id.getRange());
        Assertions.assertEquals(new Range(3, 5), def0.init.getRange());

        YanTree.ExprStmt stmt1 = (YanTree.ExprStmt) out.defs.get(1);
        YanTree.Binary expr = (YanTree.Binary) stmt1.expr;
        Assertions.assertEquals(new Range(7, 11), stmt1.getRange());
        Assertions.assertEquals(YanTree.Operator.Tag.ASSIGN, expr.op.tag);
        Assertions.assertEquals(new Range(7, 7), expr.left.getRange());
        Assertions.assertEquals(new Range(9, 11), expr.right.getRange());
    }

    @Test
    public void testFuncDef() {
        Phase<Code, YanTree.Program> parser = new YanLexer().pipe(new YanParser());
    }
}
