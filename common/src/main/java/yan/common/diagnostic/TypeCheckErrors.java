package yan.common.diagnostic;

import yan.foundation.driver.log.Diagnostic;
import yan.foundation.frontend.ast.Tree;
import yan.foundation.frontend.lex.Token;
import yan.foundation.frontend.semantic.v1.Type;

public interface TypeCheckErrors {

    default Diagnostic UnsupportedOperandTypes(Type lhsType, Type rhsType, String operator, Token anchor) {
        var diagnostic = Diagnostic.Error(String.format("unsupported operand types for %s: '%s' and '%s'",
                                                        operator, lhsType.toString(), rhsType.toString()));
        Errors.fillRangePosition(anchor, diagnostic, true);
        return diagnostic;
    }

    default Diagnostic UnsupportedOperandType(Type type, String operator, Token anchor) {
        var diagnostic = Diagnostic.Error(String.format("unsupported operand type for %s: '%s'",
                                                        operator, type.toString()));
        Errors.fillRangePosition(anchor, diagnostic, true);
        return diagnostic;
    }

    default Diagnostic ReturnValueInVoidFunction(Tree returnTree, Tree funcTree) {
        var diagnostic = Diagnostic.Error("void function should not return a value");
        Errors.fillRangePosition(returnTree.start, diagnostic, true);
        return diagnostic;
    }

    default Diagnostic WrongType(Token anchor, Type expected, Type actual) {
        var diagnostic = Diagnostic.Error("expect type " + expected + ", got " + actual);
        Errors.fillRangePosition(anchor, diagnostic, true);
        return diagnostic;
    }
}
