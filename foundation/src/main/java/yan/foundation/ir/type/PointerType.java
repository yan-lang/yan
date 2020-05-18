package yan.foundation.ir.type;

public class PointerType extends IRType {
    IRType elementType;

    private PointerType(IRType elementType) {
        super(Kind.POINTER);
        this.elementType = elementType;
    }

    public static PointerType get(IRType elementType) {
        return new PointerType(elementType);
    }

    public IRType getElementType() { return elementType; }

    @Override
    public boolean isPointerType() {
        return true;
    }

    @Override
    public String toString() {
        return elementType + "*";
    }
}
