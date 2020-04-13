import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import yan.foundation.compiler.frontend.ast.Range;
import yan.foundation.compiler.frontend.parse.Parser;
import yan.foundation.driver.lang.Code;
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
        Assertions.assertEquals(def0.id.range, new Range(1, 1));
        Assertions.assertEquals(def0.init.range, new Range(3, 5));

        YanTree.ExprStmt stmt1 = (YanTree.ExprStmt) out.defs.get(1);
        YanTree.Binary expr = (YanTree.Binary) stmt1.expr;
        Assertions.assertEquals(stmt1.range, new Range(7, 12));
        Assertions.assertEquals(expr.op.tag, YanTree.Operator.Tag.ASSIGN);
        Assertions.assertEquals(expr.left.range, new Range(7, 7));
        Assertions.assertEquals(expr.right.range, new Range(9, 11));
    }

    public void testExpr() {

    }
}
