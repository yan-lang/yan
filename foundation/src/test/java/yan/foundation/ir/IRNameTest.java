package yan.foundation.ir;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import yan.foundation.ir.type.FunctionType;
import yan.foundation.ir.type.VoidType;

public class IRNameTest {
    @Test
    public void testBasicBlock() {
        Function function = new Function(FunctionType.get(VoidType.get()), "f");
        BasicBlock block1 = new BasicBlock("if.then", function);
        BasicBlock block2 = new BasicBlock("if.then", function);
        Assertions.assertEquals("if.then", block1.getName());
        Assertions.assertEquals("if.then1", block2.getName());
    }
}
