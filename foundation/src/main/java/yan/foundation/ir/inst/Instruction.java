package yan.foundation.ir.inst;

import yan.foundation.ir.BasicBlock;
import yan.foundation.ir.User;
import yan.foundation.ir.type.IRType;

public abstract class Instruction extends User {

    private BasicBlock parent;

    public Instruction(IRType type, int numOps, BasicBlock parent) {
        super(type, numOps);
        setParent(parent);
    }

    public BasicBlock getParent() {
        return parent;
    }

    public void setParent(BasicBlock parent) {
        this.parent = parent;
    }

    public <T> T accept(InstVisitor<T> visitor) {
        return visitor.visit(this);
    }

    public void accept(InstVoidVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return "%" + getName();
    }

    @Override
    public void setName(String name) {
        if (name == null || name.equals("")) {
            super.setName(String.valueOf(parent.getParent().getAvailableTempID()));
        } else {
            var resolvedName = parent.getParent().resolveNameConflict(name);
            super.setName(resolvedName);
        }
    }
}
