package yan.foundation.frontend.semantic.v1.type;

import yan.foundation.frontend.semantic.v1.Type;

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
