package yan.foundation.ir;

import yan.foundation.ir.type.IRType;

import java.util.ArrayList;
import java.util.List;

/**
 * Value is a fundamental class in our llvm like intermediate representation.
 * Almost any component in a intermediate language tree is a Value.
 */
public abstract class Value {
    protected String name;
    protected IRType type;

    protected List<User> users = new ArrayList<>();

    public Value(IRType type) {
        this.type = type;
    }

    public void replaceAllUseWith(Value v) {
    }

    public IRType getType() { return type; }

    public boolean hasName() { return name != null; }

    public String getName() { return name; }

    public void setName(String name) {
        // "" is not considered a name,
        if (name.equals("")) return;
        this.name = name;
    }

}
