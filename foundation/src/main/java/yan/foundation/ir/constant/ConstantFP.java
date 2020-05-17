package yan.foundation.ir.constant;

import yan.foundation.ir.Constant;
import yan.foundation.ir.type.FloatType;

public class ConstantFP extends Constant {
    public final double value;

    public ConstantFP(FloatType type, double value) {
        super(type, 0);
        this.value = value;
    }
}
