package yan.foundation.frontend.semantic.v1.symbol;

import yan.foundation.frontend.semantic.v1.Symbol;
import yan.foundation.frontend.semantic.v1.Type;

public abstract class TypeSymbol extends Symbol {
    public TypeSymbol(String name) {
        super(name);
    }

    abstract public Type getType();
}
