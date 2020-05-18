package yan.foundation.ir.type;

import yan.foundation.ir.constant.ConstantFP;

public class FloatType extends IRType {
    public FloatType(Kind kind) {
        super(kind);
        assert kind == Kind.FLOAT || kind == Kind.DOUBLE;
    }

    public static final FloatType Float = new FloatType(Kind.FLOAT);
    public static final FloatType Double = new FloatType(Kind.DOUBLE);

    // Constant

    public ConstantFP constant(double value) { return new ConstantFP(this, value); }

    @Override
    public boolean isFloatingPointType() {
        return true;
    }

    @Override
    public String toString() {
        return kind == Kind.FLOAT ? "float" : "double";
    }
}
