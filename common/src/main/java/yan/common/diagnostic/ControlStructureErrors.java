package yan.common.diagnostic;

import yan.foundation.driver.log.Diagnostic;
import yan.foundation.frontend.ast.Tree;

public interface ControlStructureErrors {
    default Diagnostic BreakNotInLoop(Tree that) {
        return getDiagnostic(that, "break", "loop");
    }

    default Diagnostic ContinueNotInLoop(Tree that) {
        return getDiagnostic(that, "continue", "loop");
    }

    default Diagnostic ReturnNotInFunction(Tree that) {
        return getDiagnostic(that, "return", "function");
    }

    private Diagnostic getDiagnostic(Tree that, String item, String container) {
        Diagnostic diagnostic = Diagnostic.Error(String.format("'%s' is only allowed inside a %s", item, container));
        Errors.fillRangePosition(that.start, diagnostic, false);
        return diagnostic;
    }
}
