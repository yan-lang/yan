package yan.common.diagnostic;

import yan.foundation.driver.log.Diagnostic;
import yan.foundation.frontend.ast.Tree;

public interface NameResolveErrors {
    default Diagnostic InvalidSymbol(Tree that, String expected) {
        var diagnostic = Diagnostic.Error(that.toStringTree() + " is not a " + expected);
        Errors.fillRangePosition(that.start, diagnostic, true);
        return diagnostic;
    }

    default Diagnostic SymbolAlreadyDefined(String name, Tree that) {
        var diagnostic = Diagnostic.Error(name + " has been already defined");
        Errors.fillRangePosition(that.start, diagnostic, false);
        return diagnostic;
    }

    default Diagnostic SymbolNotDefined(String name, Tree that) {
        var diagnostic = Diagnostic.Error(name + " has not been defined");
        Errors.fillRangePosition(that.start, diagnostic, false);
        return diagnostic;
    }
}
