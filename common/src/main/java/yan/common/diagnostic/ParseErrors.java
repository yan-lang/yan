package yan.common.diagnostic;

import yan.foundation.driver.log.Diagnostic;
import yan.foundation.frontend.ast.Tree;
import yan.foundation.frontend.parse.BaseParserDiagnostic;

public interface ParseErrors extends BaseParserDiagnostic.IErrors {
    default Diagnostic InvalidAssignmentTarget(Tree tree) {
        Diagnostic d = Diagnostic.Error("invalid assignment target");
        Errors.fillRangePosition(tree.start, d, true);
        return d;
    }

    default Diagnostic InvalidFunctionName(Tree tree) {
        Diagnostic d = Diagnostic.Error("invalid function name");
        Errors.fillRangePosition(tree.start, d, true);
        return d;
    }

}
