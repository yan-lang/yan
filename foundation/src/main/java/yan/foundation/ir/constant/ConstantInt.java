package yan.foundation.ir.constant;

import yan.foundation.ir.Constant;
import yan.foundation.ir.type.IntegerType;

/**
 * This is the shared class of boolean and integer constants. This class
 * represents both boolean and integral constants.
 * Class for constant integers.
 */
public class ConstantInt extends Constant {
    public final int value;

    public ConstantInt(IntegerType type, int value) {
        super(type, 0);
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
