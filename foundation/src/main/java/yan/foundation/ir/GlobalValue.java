package yan.foundation.ir;

import yan.foundation.ir.type.IRType;
import yan.foundation.ir.type.PointerType;

public abstract class GlobalValue extends Constant {
    public GlobalValue(IRType type, int numOps) {
        super(PointerType.get(type), numOps);
    }

    @Override
    public String toString() {
        return "@" + getName();
    }

    @Override
    public PointerType getType() {
        return (PointerType) super.getType();
    }

    abstract public boolean isDeclaration();
}
