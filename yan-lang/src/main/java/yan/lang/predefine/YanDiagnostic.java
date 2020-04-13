package yan.lang.predefine;

import yan.foundation.compiler.frontend.parse.BaseParserDiagnostic;
import yan.foundation.driver.log.Diagnostic;

public class YanDiagnostic {
    public static IErrors Errors = new IErrors() {
    };

    public interface IErrors extends BaseParserDiagnostic.IErrors {
        default Diagnostic TopLevelStatement(YanTree tree) {
            return new Diagnostic("TopLevelStatement");
        }

        default Diagnostic UnknownTopLevelDefinition() {
            return new Diagnostic("UnknownTopLevelDefinition");

        }

        default Diagnostic InvalidAssignmentTarget() {
            return new Diagnostic("InvalidAssignmentTarget");
        }

        default Diagnostic ConsecutiveStatements() {
            return new Diagnostic("ConsecutiveStatements");
        }
    }
}
