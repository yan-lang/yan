package yan.foundation.ir;

import yan.foundation.ir.type.IRType;

public class Parameter extends Value {
    public final Function parent;
    public final int paramNum;

    public Parameter(IRType type, Function parent, int paramNum) {
        super(type);
        this.parent = parent;
        this.paramNum = paramNum;
    }

    @Override
    public String toString() {
        return "%" + paramNum;
    }
}
