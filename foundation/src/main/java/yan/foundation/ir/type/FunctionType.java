package yan.foundation.ir.type;

import java.util.List;

public class FunctionType extends IRType {
    private final IRType returnType;
    private final List<IRType> paramTypes;

    private FunctionType(IRType returnType, List<IRType> paramTypes) {
        super(Kind.FUNCTION);
        this.returnType = returnType;
        this.paramTypes = paramTypes;
    }

    public static FunctionType get(IRType returnType, List<IRType> paramTypes) {
        return new FunctionType(returnType, paramTypes);
    }

    public static FunctionType get(IRType returnType) {
        return new FunctionType(returnType, List.of());
    }

    public IRType getReturnType() { return returnType; }

    public List<IRType> getParamTypes() { return paramTypes; }

    public IRType getParamTypeAt(int index) { return paramTypes.get(index); }

    public int numOfParams() { return paramTypes.size(); }
}
