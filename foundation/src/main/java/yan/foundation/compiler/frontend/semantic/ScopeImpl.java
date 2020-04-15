package yan.foundation.compiler.frontend.semantic;

import java.util.LinkedHashMap;
import java.util.Map;

public class ScopeImpl implements Scope {
    String name;
    Scope enclosingScope;
    Map<String, Symbol> symbols = new LinkedHashMap<>();

    public ScopeImpl(String name, Scope enclosingScope) {
        this.name = name;
        this.enclosingScope = enclosingScope;
    }

    @Override
    public String getScopeName() { return name; }

    @Override
    public Scope getEnclosingScope() { return enclosingScope; }

    @Override
    public Map<String, Symbol> getMembers() { return symbols; }

    public String toString() { return symbols.keySet().toString(); }

}
