package yan.foundation.ir.constant;

import yan.foundation.ir.Constant;
import yan.foundation.ir.type.StructType;

import java.util.List;
import java.util.stream.Collectors;

public class ConstantStruct extends Constant {
    public final List<Constant> members;

    public ConstantStruct(StructType type, List<Constant> members) {
        super(type, 0);
        this.members = members;
        assertOK();
    }

    private void assertOK() {

    }

    @Override
    public String toString() {
        return "{" + members.stream().map(Object::toString).collect(Collectors.joining(", ")) + "}";
    }
}
