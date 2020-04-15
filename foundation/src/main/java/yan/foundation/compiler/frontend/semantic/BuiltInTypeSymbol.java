package yan.foundation.compiler.frontend.semantic;

public class BuiltInTypeSymbol extends Symbol implements Type {
    public BuiltInTypeSymbol(String name) {
        super(name, null);
    }

    @Override
    public String getTypeName() {
        return name;
    }
}
