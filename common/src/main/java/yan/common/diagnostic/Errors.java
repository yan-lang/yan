package yan.common.diagnostic;

import yan.foundation.driver.log.Diagnostic;
import yan.foundation.frontend.lex.Token;

public class Errors {
    public static ControlStructureErrors ControlStructure = new ControlStructureErrors() {};
    public static LexErrors Lex = new LexErrors() {};
    public static ParseErrors Parse = new ParseErrors() {};
    public static NameResolveErrors NameResolve = new NameResolveErrors() {};
    public static TypeCheckErrors TypeCheck = new TypeCheckErrors() {};

    public static void fillRangePosition(Token anchor, Diagnostic d, boolean fillLength) {
        d.line = anchor.line;
        d.column = anchor.col;
        if (fillLength) d.length = anchor.stop - anchor.start;
        d.sourceName = anchor.source.getSourceName();
        d.context = anchor.source.get(d.line);
    }
}
