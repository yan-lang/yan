package yan.common.diagnostic;

import yan.foundation.compiler.frontend.lex.Token;
import yan.foundation.driver.log.Diagnostic;

public class Errors {
    public static ControlStructureErrors ControlStructure = new ControlStructureErrors() {};
    public static LexErrors Lex = new LexErrors() {};
    public static ParseErrors Parse = new ParseErrors() {};
    public static NameResolveErrors NameResolve = new NameResolveErrors() {};
    public static TypeCheckErrors TypeCheck = new TypeCheckErrors() {};

    protected static void fillRangePosition(Token anchor, Diagnostic d, boolean fillLength) {
        d.line = anchor.line;
        d.column = anchor.col;
        if (fillLength) d.length = anchor.stop - anchor.start;
        d.sourceName = anchor.source.getSourceName();
        d.context = anchor.source.get(d.line);
    }
}
