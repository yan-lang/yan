package yan.foundation.frontend.semantic.v1.type;

import yan.foundation.frontend.semantic.v1.Type;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MethodType extends Type {
    public Type retType;
    public List<Type> argTypes = new ArrayList<>();

    public MethodType(Type retType, List<Type> argTypes) {
        this.retType = retType;
        this.argTypes = argTypes;
    }

    public MethodType(Type retType) {
        this.retType = retType;
        argTypes = List.of();
    }

    public MethodType() { }

    @Override
    public String toString() {
        return String.format("(%s)->%s",
                             argTypes.stream().map(Type::toString).collect(Collectors.joining(",")),
                             retType.toString());
    }
}
