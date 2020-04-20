package yan.foundation.compiler.frontend.semantic.v1.symbol;

import yan.foundation.compiler.frontend.semantic.v1.Symbol;
import yan.foundation.compiler.frontend.semantic.v1.type.MethodType;

public class MethodSymbol extends Symbol {
    public MethodType methodType;

    public MethodSymbol(String name, MethodType type) {
        super(name);
        this.methodType = type;
    }
}
