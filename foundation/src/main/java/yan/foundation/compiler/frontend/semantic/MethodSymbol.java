package yan.foundation.compiler.frontend.semantic;

import yan.foundation.compiler.frontend.ast.Tree;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class MethodSymbol extends ScopedSymbol {
    Type retType;
    Map<String, Symbol> orderedParams = new LinkedHashMap<>();

    public MethodSymbol(String name, Type retType, Tree tree, Scope enclosingScope) {
        super(name, tree, enclosingScope);
        this.retType = retType;
    }

    @Override
    public Map<String, Symbol> getMembers() {
        return orderedParams;
    }

    @Override
    public String toString() {
        String argString = orderedParams.values()
                                        .stream().map(symbol -> symbol.name)
                                        .collect(Collectors.joining(","));
        return String.format("MethodSymbol(%s)[(%s) -> %s]", name, argString, retType.getTypeName());
    }
}
