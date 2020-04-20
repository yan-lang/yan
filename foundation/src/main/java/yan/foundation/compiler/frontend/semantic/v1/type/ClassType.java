package yan.foundation.compiler.frontend.semantic.v1.type;

import yan.foundation.compiler.frontend.semantic.v1.Type;

public class ClassType extends Type {
    public ClassType parentType;
    public String name;


    @Override
    public String toString() {
        return name;
    }
}
