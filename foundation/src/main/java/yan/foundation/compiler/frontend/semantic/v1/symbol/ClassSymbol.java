package yan.foundation.compiler.frontend.semantic.v1.symbol;

import yan.foundation.compiler.frontend.semantic.v1.Type;
import yan.foundation.compiler.frontend.semantic.v1.type.ClassType;

public class ClassSymbol extends TypeSymbol {
    public ClassType classType;

    public ClassSymbol(String name, ClassType classType) {
        super(name);
        this.classType = classType;
    }

    @Override
    public Type getType() {
        return classType;
    }
}
