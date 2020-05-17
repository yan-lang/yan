package yan.foundation.ir;

import yan.foundation.ir.inst.Instruction;
import yan.foundation.ir.type.LabelType;

import java.util.Iterator;

public class BasicBlock extends Value implements Iterable<Instruction> {
    String name;
    Function parent;
    ValueSymbolTable<Instruction> instructions = new ValueSymbolTable<>();

    public BasicBlock(String name, Function parent) {
        super(LabelType.get());
        this.name = name;
        this.parent = parent;

        insertInto(parent);
    }

    private void insertInto(Function parent) {
        if (parent != null) {
            parent.basicBlocks.add(this);
        }
    }

    @Override
    public Iterator<Instruction> iterator() {
        return instructions.iterator();
    }
}
