package yan.foundation.ir;

import yan.foundation.ir.type.IRType;

public abstract class Constant extends User {

    public Constant(IRType type, int numOps) {
        super(type, numOps);
    }
}
