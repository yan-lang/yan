package yan.foundation.ir.constant;

import yan.foundation.ir.Constant;
import yan.foundation.ir.type.StructType;

import java.util.List;

public class ConstantStruct extends Constant {
    public final List<Constant> members;

    public ConstantStruct(StructType type, List<Constant> members) {
        super(type, 0);
        this.members = members;
        assertOK();
    }

    private void assertOK() {

    }
}
