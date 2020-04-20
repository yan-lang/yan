package yan.foundation.compiler.frontend.semantic.v1.type;

import yan.foundation.compiler.frontend.semantic.v1.Type;

public class PrimitiveType extends Type {
    public String name;

    public PrimitiveType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
