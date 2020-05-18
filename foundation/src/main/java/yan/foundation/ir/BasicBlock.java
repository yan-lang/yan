package yan.foundation.ir;

import yan.foundation.ir.inst.Instruction;
import yan.foundation.ir.type.LabelType;

import java.util.Iterator;

public class BasicBlock extends Value implements Iterable<Instruction> {
    Function parent;
    ValueSymbolTable<Instruction> instructions = new ValueSymbolTable<>();

    public BasicBlock(String name, Function parent) {
        super(LabelType.get());
        this.name = name;
        this.parent = parent;

        insertInto(parent);
        setName(name);
    }

    private void insertInto(Function parent) {
        if (parent != null) {
            parent.basicBlocks.add(this);
        }
    }

    public Function getParent() {
        return parent;
    }

    @Override
    public Iterator<Instruction> iterator() {
        return instructions.iterator();
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public void setName(String name) {
        if (name == null || name.equals("")) {
            super.setName(String.valueOf(parent.getAvailableTempID()));
        } else {
            var resolvedName = parent.resolveNameConflict(name);
            super.setName(resolvedName);
        }
    }
}
