package yan.foundation.ir.type;

public class LabelType extends IRType {
    public LabelType() {
        super(Kind.LABEL);
    }

    public static LabelType get() { return instance; }

    public static LabelType instance = new LabelType();

    @Override
    public boolean isLabelType() {
        return true;
    }
}
