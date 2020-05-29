package yan.common.diagnostic;

import yan.foundation.driver.log.Diagnostic;
import yan.foundation.frontend.ast.Tree;
import yan.foundation.frontend.lex.Token;
import yan.foundation.frontend.semantic.v1.Type;

public interface TypeCheckErrors {

    default Diagnostic UnsupportedOperandTypes(Type lhsType, Type rhsType, String operator, Token anchor) {
        var diagnostic = Diagnostic.Error(String.format("unsupported operand types for %s: '%s' and '%s'",
                                                        operator, lhsType.toString(), rhsType.toString()));
        return Errors.fillRangePosition(anchor, diagnostic, true);
    }

    default Diagnostic UnsupportedOperandType(Type type, String operator, Token anchor) {
        var diagnostic = Diagnostic.Error(String.format("unsupported operand type for %s: '%s'",
                                                        operator, type.toString()));
        return Errors.fillRangePosition(anchor, diagnostic, true);
    }

    default Diagnostic ReturnValueInVoidFunction(Tree returnTree, Tree funcTree) {
        var diagnostic = Diagnostic.Error("void function should not return a value");
        return Errors.fillRangePosition(returnTree, diagnostic, false);
    }

    default Diagnostic WrongType(Tree anchor, Type expected, Type actual) {
        var diagnostic = Diagnostic.Error("expect type " + expected + ", got " + actual);
        return Errors.fillRangePosition(anchor, diagnostic, true);
    }
}
