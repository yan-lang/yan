package yan.foundation.compiler.frontend.semantic.v1.type;

import yan.foundation.compiler.frontend.semantic.v1.Type;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MethodType extends Type {
    public List<Type> argTypes = new ArrayList<>();
    public Type retType;


    @Override
    public String toString() {
        return String.format("(%s)->%s",
                             argTypes.stream().map(Type::toString).collect(Collectors.joining(",")),
                             retType.toString());
    }
}
