package yan.common.diagnostic;

import yan.foundation.compiler.frontend.ast.Tree;
import yan.foundation.compiler.frontend.parse.BaseParserDiagnostic;
import yan.foundation.driver.log.Diagnostic;

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
