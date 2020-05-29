package yan.common.diagnostic;

import yan.foundation.driver.log.Diagnostic;
import yan.foundation.frontend.ast.Tree;
import yan.foundation.frontend.lex.Token;

public class Errors {
    public static ControlStructureErrors ControlStructure = new ControlStructureErrors() {};
    public static LexErrors Lex = new LexErrors() {};
    public static ParseErrors Parse = new ParseErrors() {};
    public static NameResolveErrors NameResolve = new NameResolveErrors() {};
    public static TypeCheckErrors TypeCheck = new TypeCheckErrors() {};

    public static Diagnostic fillRangePosition(Token anchor, Diagnostic d, boolean fillLength) {
        d.line = anchor.line;
        d.column = anchor.col;
        if (fillLength) d.length = anchor.stop - anchor.start - 1;
        d.sourceName = anchor.source.getSourceName();
        d.context = anchor.source.get(d.line);
        return d;
    }

    public static Diagnostic fillRangePosition(Tree anchor, Diagnostic d, boolean fillLength) {
        d.line = anchor.start.line;
        d.column = anchor.start.col;
        if (fillLength) d.length = anchor.end.stop - anchor.start.start - 1;
        d.sourceName = anchor.start.source.getSourceName();
        d.context = anchor.start.source.get(d.line);
        return d;
    }
}
