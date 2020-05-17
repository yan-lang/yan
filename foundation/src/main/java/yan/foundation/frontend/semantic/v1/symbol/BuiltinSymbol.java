package yan.foundation.frontend.semantic.v1.symbol;

import yan.foundation.frontend.semantic.v1.Type;

public class BuiltinSymbol extends TypeSymbol {
    public Type type;

    public BuiltinSymbol(String name, Type type) {
        super(name);
        this.type = type;
    }

    @Override
    public Type getType() {
        return type;
    }
}
