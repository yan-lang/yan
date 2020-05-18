package yan.foundation.ir.type;

import yan.foundation.ir.Constant;
import yan.foundation.ir.constant.ConstantArray;

import java.util.List;

public class ArrayType extends IRType {
    IRType elementType;
    int numOfElements;

    public ArrayType(IRType elementType, int numOfElements) {
        super(Kind.ARRAY);
        this.elementType = elementType;
        this.numOfElements = numOfElements;
    }

    public static ArrayType get(IRType elementType, int size) {
        return new ArrayType(elementType, size);
    }

    public IRType getElementType() { return elementType; }

    public int numOfElements() { return numOfElements; }

    // Constant

    public ConstantArray constant(List<Constant> values) {
        return new ConstantArray(this, values);
    }

    @Override
    public boolean isArrayType() {
        return true;
    }

    @Override
    public String toString() {
        return "[" + numOfElements + " x " + elementType + "]";
    }
}
