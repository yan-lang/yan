package yan.foundation.exec;

import yan.foundation.ir.BasicBlock;
import yan.foundation.ir.Function;
import yan.foundation.ir.Module;
import yan.foundation.ir.Value;
import yan.foundation.ir.inst.Instruction;
import yan.foundation.ir.inst.Instructions;
import yan.foundation.ir.type.IRType;
import yan.foundation.ir.type.VoidType;

import java.util.ArrayList;
import java.util.List;

public class InterpreterImpl extends Interpreter {
    public InterpreterImpl(Module module) {
        super(module);
    }

    @Override
    protected void exec(Instruction inst) {
        inst.accept(this);
    }

    @Override
    public void visit(Instructions.BinaryOpInst inst) {
        var frame = rtStack.peek();
        var type = inst.getLHSOperand().getType();
        GenericValue src1 = getOperandValue(inst.getLHSOperand(), frame);
        GenericValue src2 = getOperandValue(inst.getRHSOperand(), frame);
        GenericValue r = new GenericValue();

        switch (inst.op) {
            case ADD: r.intValue = src1.intValue + src2.intValue; break;
            case SUB: r.intValue = src1.intValue - src2.intValue; break;
            case MUL: r.intValue = src1.intValue * src2.intValue; break;
            case DIV: r.intValue = src1.intValue / src2.intValue; break;
            case FADD: execFAddInst(r, src1, src2, type); break;
            case FSUB: execFSubInst(r, src1, src2, type); break;
            case FMUL: execFMulInst(r, src1, src2, type); break;
            case FDIV: execFDivInst(r, src1, src2, type); break;
            case SHL: r.intValue = src1.intValue << src2.intValue; break;
            case LSHR: r.intValue = src1.intValue >>> src2.intValue; break;
            case ASHR: r.intValue = src1.intValue >> src2.intValue; break;
            case AND: r.intValue = src1.intValue & src2.intValue; break;
            case OR: r.intValue = src1.intValue | src2.intValue; break;
            case XOR: r.intValue = src1.intValue ^ src2.intValue; break;
        }
        setValue(inst, r, frame);
    }

    @Override
    public void visit(Instructions.ICmpInst inst) {
        var frame = rtStack.peek();
        GenericValue src1 = getOperandValue(inst.getOperand(0), frame);
        GenericValue src2 = getOperandValue(inst.getOperand(1), frame);
        GenericValue r = new GenericValue();

        switch (inst.pred) {
            case ICMP_EQ: r.intValue = getIntValueFrom(src1.intValue == src2.intValue); break;
            case ICMP_GT: r.intValue = getIntValueFrom(src1.intValue > src2.intValue); break;
            case ICMP_GTE: r.intValue = getIntValueFrom(src1.intValue >= src2.intValue); break;
            case ICMP_LT: r.intValue = getIntValueFrom(src1.intValue < src2.intValue); break;
            case ICMP_LTE: r.intValue = getIntValueFrom(src1.intValue <= src2.intValue); break;
            case ICMP_NEQ: r.intValue = getIntValueFrom(src1.intValue != src2.intValue); break;
            default: throw new IllegalStateException("Unexpected value: " + inst.pred);
        }
        setValue(inst, r, frame);
    }

    public int getIntValueFrom(boolean cond) {
        return cond ? 1 : 0;
    }

    @Override
    public void visit(Instructions.FCmpInst inst) {
        var frame = rtStack.peek();
        var type = inst.getOperand(0).getType();
        GenericValue src1 = getOperandValue(inst.getOperand(0), frame);
        GenericValue src2 = getOperandValue(inst.getOperand(1), frame);
        GenericValue r = new GenericValue();

        // we promote float to double to reduce some redundant code
        double src1Val = type.kind == IRType.Kind.DOUBLE ? src1.doubleValue : src1.floatValue;
        double src2Val = type.kind == IRType.Kind.DOUBLE ? src2.doubleValue : src2.floatValue;

        switch (inst.pred) {
            case FCMP_EQ: r.intValue = getIntValueFrom(src1Val == src2Val); break;
            case FCMP_GT: r.intValue = getIntValueFrom(src1Val > src2Val); break;
            case FCMP_GTE: r.intValue = getIntValueFrom(src1Val >= src2Val); break;
            case FCMP_LT: r.intValue = getIntValueFrom(src1Val < src2Val); break;
            case FCMP_LTE: r.intValue = getIntValueFrom(src1Val <= src2Val); break;
            case FCMP_NEQ: r.intValue = getIntValueFrom(src1Val != src2Val); break;
            default: throw new IllegalStateException("Unexpected value: " + inst.pred);
        }
        setValue(inst, r, frame);
    }

    //===----------------------------------------------------------------------===//
    //                    Memory Instruction Implementations
    //===----------------------------------------------------------------------===//

    @Override
    public void visit(Instructions.AllocaInst allocaInst) {
        var frame = getCurrentFrame();
        IRType type = allocaInst.getAllocatedType();
        var value = GenericValue.ZeroInitialized(type);
        GenericValue ptr = new GenericValue();
        ptr.pointerValue = value;
        setValue(allocaInst, ptr, frame);
    }

    @Override
    public void visit(Instructions.GetElementPtrInst inst) {
        var frame = getCurrentFrame();
        var pointer = getOperandValue(inst.getPointerOperand(), frame);
        var index = getOperandValue(inst.getIndexOperand(), frame);
        if (index.intValue >= pointer.pointerValue.aggregateValue.size()) {
            throw new RuntimeError("index out of bound: " + index.intValue);
        }
        // we need to return a pointer actually
        var elementPointer = new GenericValue();
        elementPointer.pointerValue = pointer.pointerValue.aggregateValue.get(index.intValue);
        setValue(inst, elementPointer, frame);
    }

    @Override
    public void visit(Instructions.LoadInst loadInst) {
        var frame = getCurrentFrame();
        var ptr = getOperandValue(loadInst.getUnaryOperand(), frame);
        setValue(loadInst, ptr.pointerValue, frame);
    }

    @Override
    public void visit(Instructions.StoreInst storeInst) {
        var frame = getCurrentFrame();
        var value = getOperandValue(storeInst.getValue(), frame);
        var pointer = getOperandValue(storeInst.getAddr(), frame);
        pointer.pointerValue = value;
    }

    //===----------------------------------------------------------------------===//
    //                    Terminator Instruction Implementations
    //===----------------------------------------------------------------------===//

    @Override
    public void visit(Instructions.BranchInst branchInst) {
        var frame = getCurrentFrame();
        BasicBlock dest = branchInst.getSuccessor(0);
        if (branchInst.isConditional()) {
            Value cond = branchInst.getCondition();
            if (getOperandValue(cond, frame).intValue == 0) {
                dest = branchInst.getSuccessor(1);
            }
        }
        switchToNewBasicBlock(dest, frame);
    }

    private void switchToNewBasicBlock(BasicBlock dest, ExecContext frame) {
        frame.currentBlock = dest;
        frame.pc = dest.iterator();
    }

    @Override
    public void visit(Instructions.CallInst inst) {
        var frame = getCurrentFrame();
        frame.callInst = inst;

        List<GenericValue> args = new ArrayList<>();
        for (int i = 0; i < inst.getNumOfArgs(); i++) {
            args.add(getOperandValue(inst.getArgOperand(i), frame));
        }

        // resolve called function
        Function callee;
        if (inst.isIndirectCall()) {
            // indirect call store function pointer as called operand
            callee = getOperandValue(inst.getCalledOperand(), frame).function;
        } else {
            // if this is a direct call, the called operand must be an instance of function
            callee = (Function) inst.getCalledOperand();
        }

        call(callee, args);
    }

    @Override
    public void visit(Instructions.RetInst retInst) {
        IRType retType = VoidType.get();
        GenericValue result = null;

        if (!retInst.isVoidRet()) {
            retType = retInst.getReturnValue().getType();
            result = getOperandValue(retInst.getReturnValue(), getCurrentFrame());
        }

        popStackAndReturnValueToCaller(retType, result);
    }

    private void popStackAndReturnValueToCaller(IRType type, GenericValue result) {
        // pop current frame
        rtStack.pop();

        if (rtStack.empty()) {
            // finished main, save result to exit code
            if (type.isVoidType()) exitValue = null;
            else exitValue = result;
        } else {
            // If we have a previous stack frame, and we have a previous call,
            // fill in the return value...
            var callingFrame = getCurrentFrame();
            if (!callingFrame.callInst.getType().isVoidType())
                setValue(callingFrame.callInst, result, callingFrame);
            callingFrame.callInst = null;
        }
    }

    //===----------------------------------------------------------------------===//
    //                    Binary Instruction Implementations
    //===----------------------------------------------------------------------===//

    private void execFDivInst(GenericValue r, GenericValue src1, GenericValue src2, IRType type) {
        switch (type.kind) {
            case FLOAT: r.floatValue = src1.floatValue / src2.floatValue; break;
            case DOUBLE: r.doubleValue = src1.doubleValue / src2.doubleValue; break;
            default: throw new IllegalStateException("Unhandled type for FDiv instruction: " + type.kind);
        }
    }

    private void execFMulInst(GenericValue r, GenericValue src1, GenericValue src2, IRType type) {
        switch (type.kind) {
            case FLOAT: r.floatValue = src1.floatValue * src2.floatValue; break;
            case DOUBLE: r.doubleValue = src1.doubleValue * src2.doubleValue; break;
            default: throw new IllegalStateException("Unhandled type for FMul instruction: " + type.kind);
        }
    }

    private void execFSubInst(GenericValue r, GenericValue src1, GenericValue src2, IRType type) {
        switch (type.kind) {
            case FLOAT: r.floatValue = src1.floatValue - src2.floatValue; break;
            case DOUBLE: r.doubleValue = src1.doubleValue - src2.doubleValue; break;
            default: throw new IllegalStateException("Unhandled type for FSub instruction: " + type.kind);
        }
    }

    private void execFAddInst(GenericValue r, GenericValue src1, GenericValue src2, IRType type) {
        switch (type.kind) {
            case FLOAT: r.floatValue = src1.floatValue + src2.floatValue; break;
            case DOUBLE: r.doubleValue = src1.doubleValue + src2.doubleValue; break;
            default: throw new IllegalStateException("Unhandled type for FAdd instruction: " + type.kind);
        }
    }
}
