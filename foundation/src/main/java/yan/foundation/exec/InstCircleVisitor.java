package yan.foundation.exec;

import yan.foundation.ir.inst.InstVisitor;
import yan.foundation.ir.inst.Instructions;

public class InstCircleVisitor implements InstVisitor<Integer> {
    protected static InstCircleVisitor shared;

    public static InstCircleVisitor getInstance() {
        if (shared == null) shared = new InstCircleVisitor();
        return shared;
    }

    @Override
    public Integer visit(Instructions.BinaryOpInst binaryOpInst) {
        switch (binaryOpInst.op) {
            case ADD: case SUB:
            case SHL: case LSHR: case ASHR:
            case AND: case OR: case XOR: return 1;
            case FADD: case FSUB: return 2;
            case MUL: return 3;
            case FMUL: return 6;
            case DIV: case REM: return 5;
            case FDIV: case FREM: return 10;
            default: throw new IllegalStateException("Unexpected value: " + binaryOpInst.op);
        }
    }

    @Override
    public Integer visit(Instructions.AllocaInst allocaInst) {
        return 0;
    }

    @Override
    public Integer visit(Instructions.BranchInst branchInst) {
        return 2;
    }

    @Override
    public Integer visit(Instructions.CallInst callInst) {
        return 2;
    }

    @Override
    public Integer visit(Instructions.ICmpInst iCmpInst) {
        return 2;
    }

    @Override
    public Integer visit(Instructions.FCmpInst fCmpInst) {
        return 4;
    }

    @Override
    public Integer visit(Instructions.GetElementPtrInst getElementPtrInst) {
        return 2;
    }

    @Override
    public Integer visit(Instructions.LoadInst loadInst) {
        return 4;
    }

    @Override
    public Integer visit(Instructions.StoreInst storeInst) {
        return 4;
    }

    @Override
    public Integer visit(Instructions.RetInst retInst) {
        return 2;
    }

    @Override
    public Integer visit(Instructions.FPToSIInst inst) {
        return 3;
    }

    @Override
    public Integer visit(Instructions.SIToFPInst inst) {
        return 3;
    }
}
