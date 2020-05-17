package yan.lang.predefine;

import yan.foundation.frontend.semantic.v1.Type;
import yan.foundation.frontend.semantic.v1.type.PrimitiveType;

public final class YanTypes {
    public static final PrimitiveType Error = new PrimitiveType("error");
    public static final PrimitiveType Void = new PrimitiveType("void");
    public static final PrimitiveType Bool = new PrimitiveType("bool");
    public static final PrimitiveType Int = new PrimitiveType("int");
    public static final PrimitiveType Double = new PrimitiveType("double");
    public static final PrimitiveType Float = new PrimitiveType("float");

    public static boolean isErrorType(Type type) {
        return type == Error;
    }
}
