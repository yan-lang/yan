package yan.foundation.frontend.semantic.v1.symbol;

import yan.foundation.frontend.semantic.v1.Symbol;
import yan.foundation.frontend.semantic.v1.Type;

public class VarSymbol extends Symbol {
    public Type type;

    public VarSymbol(String name, Type type) {
        super(name);
        this.type = type;
    }

    public VarSymbol(String name) {
        super(name);
    }


}
