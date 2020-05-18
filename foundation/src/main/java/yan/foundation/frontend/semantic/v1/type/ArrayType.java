package yan.foundation.frontend.semantic.v1.type;

import yan.foundation.frontend.semantic.v1.Type;

public class ArrayType extends Type {
    public Type elementType;
    public int size;

    @Override
    public String toString() {
        return elementType + "[" + size + "]";
    }
}