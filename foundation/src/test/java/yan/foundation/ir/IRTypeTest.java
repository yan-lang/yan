package yan.foundation.ir;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import yan.foundation.ir.type.*;

import java.util.List;

public class IRTypeTest {

    @Test
    public void testToString() {
        var fType = FunctionType.get(IntegerType.int32, List.of(IntegerType.int32, FloatType.Double));
        var arrayType = ArrayType.get(fType, 10);
        var vtableType = StructType.get("T.Vtable", List.of(PointerType.get(fType),
                                                            PointerType.get(arrayType)));
        System.out.println(fType);
        System.out.println(vtableType);
        System.out.println(VoidType.get());
        System.out.println(arrayType);

        Assertions.assertEquals("i32 (i32, double)", fType.toString());
//        Assertions.assertEquals("{i32 (i32, double)*, [10 x i32 (i32, double)]*}", vtableType.toString());
        Assertions.assertEquals("void", VoidType.get().toString());
        Assertions.assertEquals("[10 x i32 (i32, double)]", arrayType.toString());
    }
}
