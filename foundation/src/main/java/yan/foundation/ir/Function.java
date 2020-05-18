package yan.foundation.ir;

import yan.foundation.ir.type.FunctionType;
import yan.foundation.ir.type.PointerType;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Function extends GlobalValue implements Iterable<BasicBlock> {
    protected List<BasicBlock> basicBlocks = new ArrayList<>();
    protected Parameter[] parameters;

    public Function(FunctionType type, String name) {
        super(type, 0);
        buildParameters(type);
        setName(name);
    }

    private void buildParameters(FunctionType type) {
        parameters = new Parameter[type.numOfParams()];
        for (int i = 0; i < parameters.length; i++) {
            parameters[i] = new Parameter(type.getParamTypeAt(i), this, i);
        }
    }

    public static Function create(FunctionType type, String name) {
        return new Function(type, name);
    }

    public int numOfParameters() {
        return parameters.length;
    }

    public Parameter parameterAt(int index) { return parameters[index]; }

    // ------------ Manipulate basic block ------------ //

    public BasicBlock appendBasicBlock(String name) {
        return new BasicBlock(name, this);
    }

    public BasicBlock entryBlock() { return basicBlocks.get(0); }

    @Override
    public Iterator<BasicBlock> iterator() {
        return basicBlocks.iterator();
    }

    public FunctionType getFunctionType() {
        return (FunctionType) ((PointerType) super.getType()).getElementType();
    }
}
