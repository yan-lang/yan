package yan.foundation.ir;

import yan.foundation.ir.type.IRType;

public abstract class User extends Value {

    protected Value[] operands;

    public User(IRType type, int numOps) {
        super(type);
        this.operands = new Value[numOps];
    }

    public Value[] getOperands() { return operands; }

    public Value getOperand(int index) { return operands[index]; }

    public void setOperand(int index, Value value) { operands[index] = value; }

    public void setOperands(Value... values) {
        assert values.length <= operands.length : "too many operands to set";
        for (int i = 0; i < values.length; i++) setOperand(i, values[i]);
    }

    public int numOfOperands() { return operands.length; }

}
