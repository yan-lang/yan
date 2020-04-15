package yan.foundation.compiler.frontend.semantic;

public class ScopeFactory {
    public static ScopeImpl GlobalScope() {
        return new ScopeImpl("global", null);
    }

    public static ScopeImpl LocalScope(Scope parent) {
        return new ScopeImpl("local", parent);
    }
}
