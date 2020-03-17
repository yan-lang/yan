import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import yan.foundation.compiler.frontend.ast.Range;
import yan.foundation.compiler.frontend.parse.Parser;
import yan.foundation.driver.BaseConfig;
import yan.lang.YanLexer;
import yan.lang.YanParser;
import yan.lang.predefine.YanTree;

public class TestParser {
    @Test
    public void testRange() {
        BaseConfig config = new BaseConfig();
        config.source = "stdin";
        YanLexer lexer = new YanLexer("lexer", config);
        Parser<YanTree.Program> parser = new YanParser("parser", config);
        var tokens = lexer.transform("var a = 10 * b\nb = 10*9");
        YanTree.Program out = parser.parse(tokens);

        YanTree.VarDef stmt0 = (YanTree.VarDef) out.stmts.get(0);
        Assertions.assertEquals(stmt0.identifier.range, new Range(1, 1));
        Assertions.assertEquals(stmt0.initializer.range, new Range(3, 5));

        YanTree.ExprStmt stmt1 = (YanTree.ExprStmt) out.stmts.get(1);
        YanTree.Assign expr = (YanTree.Assign) stmt1.expr;
        Assertions.assertEquals(stmt1.range, new Range(7, 12));
        Assertions.assertEquals(expr.identifier.range, new Range(7, 7));
        Assertions.assertEquals(expr.expr.range, new Range(9, 11));
    }

}
