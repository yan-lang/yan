package yan.foundation.ir;

import yan.foundation.ir.type.FunctionType;

import java.util.*;

public class Function extends GlobalValue implements Iterable<BasicBlock> {
    protected List<BasicBlock> basicBlocks = new ArrayList<>();
    protected Parameter[] parameters;

    // Count local temps
    protected int tmpCount;
    protected Map<String, Integer> nameCount = new HashMap<>();

    public Function(FunctionType type, String name) {
        super(type, 0);
        buildParameters(type);
        setName(name);
        tmpCount = parameters.length;
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
        return (FunctionType) super.getType().getElementType();
    }

    public int getAvailableTempID() {
        return tmpCount++;
    }

    public String resolveNameConflict(String name) {
        if (nameCount.containsKey(name)) {
            var newName = name + nameCount.get(name);
            nameCount.put(name, nameCount.get(name) + 1);
            return newName;
        } else {
            nameCount.put(name, 1);
            return name;
        }
    }
}
