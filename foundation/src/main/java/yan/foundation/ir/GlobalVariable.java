package yan.foundation.ir;

import yan.foundation.ir.type.IRType;

public class GlobalVariable extends GlobalValue {
    public final boolean isConstantGlobal;

    public GlobalVariable(IRType type, Constant initializer, boolean isConstantGlobal, String name) {
        super(type, 1);
        this.isConstantGlobal = isConstantGlobal;
        setName(name);
        if (initializer != null) {
            assert type == initializer.getType() :
                    "Initializer should be the same type as the GlobalVariable!";
            setOperand(0, initializer);
        }
    }

    public void setInitializer(Constant initializer) {
        setOperand(0, initializer);
    }

    public Constant getInitializer() {
        return (Constant) getOperand(0);
    }

    public boolean hasInitializer() {
        return getOperand(0) != null;
    }

}
