package yan.foundation.ir.constant;

import yan.foundation.ir.Constant;
import yan.foundation.ir.type.ArrayType;

import java.util.List;
import java.util.stream.Collectors;

public class ConstantArray extends Constant {
    public final List<Constant> elements;

    public ConstantArray(ArrayType type, List<Constant> elements) {
        super(type, 0);
        this.elements = elements;
    }

    @Override
    public String toString() {
        return "{" + elements.stream().map(Object::toString).collect(Collectors.joining(", ")) + "}";
    }
}
