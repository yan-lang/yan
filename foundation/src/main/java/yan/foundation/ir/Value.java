package yan.foundation.ir;

import yan.foundation.ir.type.IRType;

public abstract class Value {
    protected String name;
    protected IRType type;

    public Value(IRType type) {
        this.type = type;
    }

    public void replaceAllUseWith(Value v) { }

    public IRType getType() { return type; }

    public boolean hasName() { return name != null; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }
}
