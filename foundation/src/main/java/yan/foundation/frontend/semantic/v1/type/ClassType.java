package yan.foundation.frontend.semantic.v1.type;

import yan.foundation.frontend.semantic.v1.Type;

public class ClassType extends Type {
    public ClassType parentType;
    public String name;

    public ClassType(ClassType parentType, String name) {
        this.parentType = parentType;
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
