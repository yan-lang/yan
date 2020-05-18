package yan.foundation.ir.type;

public class VoidType extends IRType {
    public static VoidType instance = new VoidType();

    public static VoidType get() { return instance; }

    private VoidType() {
        super(Kind.VOID);
    }

    @Override
    public boolean isVoidType() {
        return true;
    }

    @Override
    public String toString() {
        return "void";
    }
}
