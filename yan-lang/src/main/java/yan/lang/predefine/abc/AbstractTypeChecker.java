package yan.lang.predefine.abc;

import yan.foundation.driver.lang.Phase;
import yan.foundation.driver.log.Diagnostic;
import yan.foundation.frontend.semantic.v1.Type;
import yan.lang.predefine.YanTree;
import yan.lang.predefine.YanTypes;

import static yan.lang.predefine.YanTypes.*;

/**
 * TypeChecker负责检查程序中所有表达式的类型是否符合语义要求。
 *
 * <p>具体要求包括:</p>
 * <ol>
 *     <li>表达式操作数要兼容</li>
 *     <ol>
 *         <li>赋值语句需要匹配类型</li>
 *         <li></li>
 *     </ol>
 *     <li>条件和循环语句的条件表达式必须是布尔类型</li>
 *     <li>函数实参和形参类型需要匹配</li>
 *     <li>return语句的返回值类型要与函数声明匹配</li>
 * </ol>
 */
public abstract class AbstractTypeChecker extends Phase<YanTree.Program, YanTree.Program>
        implements YanTree.VoidVisitor {

    protected boolean checkType(Type expected, Type actual) {
        if (isErrorType(actual)) return false;
        if (compatible(actual, expected)) {
            return true;
        } else {
            logger.log(Diagnostic.Error("expect type " + expected + ", got " + actual));
            return false;
        }
    }

    protected boolean compatible(Type operandType, Type castedType) {
        return operandType == castedType;
    }

    protected boolean compatible(YanTree.Operator.Tag op, Type operandType) {
        return switch (op) {
            case MINUS -> operandType != Bool;
            case REL_NOT -> operandType == Bool;
            default -> throw new IllegalStateException("Unexpected value: " + op);
        };
    }

    protected boolean compatible(YanTree.Operator.Tag op, Type lhsType, Type rhsType) {
        if (op == YanTree.Operator.Tag.EXP) return rhsType == Int;
        return lhsType == rhsType;
    }

    /**
     * Get result type of a unary expression.
     *
     * @param op          unary operator
     * @param operandType operand type of the unary expression
     * @return inferred type after unary operation
     */
    protected Type getResultType(YanTree.Operator.Tag op, Type operandType) {
        // There are only two unary operations for now:
        // 1. negative(-1,-1.0),
        // 2. not(!bool_expression)
        // as we can see, the result type is the same as operand type in both cases.
        // Even though, we can not just return operandType, because it could be ErrorType.
        switch (op) {
            case MINUS:
                // we guess the result type is int when the operandType is ErrorType,
                // since int can always be converted to other arithmetic type.
                if (isErrorType(operandType)) return YanTypes.Int;
                else return operandType;
            case REL_NOT:
                return YanTypes.Bool;
            default:
                throw new IllegalStateException("Unexpected value: " + op);
        }
    }

    protected Type getResultType(YanTree.Operator.Tag tag, Type lhsType, Type rhsType) {
        return switch (tag) {
            case PLUS, MINUS, MULTI, DIV, ASSIGN, EXP -> lhsType;
            case EQ, NEQ, GT, GTE, LT, LTE, REL_OR, REL_AND, REL_NOT -> Bool;
        };
    }

    @Override
    public YanTree.Program transform(YanTree.Program input) {
        input.accept(this);
        return input;
    }
}
