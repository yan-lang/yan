package yan.foundation.exec;

import yan.foundation.ir.BasicBlock;
import yan.foundation.ir.Function;
import yan.foundation.ir.Value;
import yan.foundation.ir.inst.Instruction;
import yan.foundation.ir.inst.Instructions;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ExecContext {
    public Function currentFunction;
    public BasicBlock currentBlock;
    public Iterator<Instruction> pc;

    public Map<Value, GenericValue> values = new HashMap<>();

    // When call other function in this function, we need to save return value of this call.
    public Instructions.CallInst callInst;

    public ExecContext(Function currentFunction) {
        this.currentFunction = currentFunction;
        this.currentBlock = currentFunction.entryBlock();
        this.pc = currentBlock.iterator();
    }
}
