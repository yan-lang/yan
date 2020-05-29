package yan.foundation.ir.inst;

public interface InstVisitor<T> {
    T visit(Instructions.BinaryOpInst binaryOpInst);

    T visit(Instructions.AllocaInst allocaInst);

    T visit(Instructions.BranchInst branchInst);

    T visit(Instructions.CallInst callInst);

    T visit(Instructions.ICmpInst iCmpInst);

    T visit(Instructions.FCmpInst fCmpInst);

    T visit(Instructions.GetElementPtrInst getElementPtrInst);

    T visit(Instructions.LoadInst loadInst);

    T visit(Instructions.StoreInst storeInst);

    T visit(Instructions.RetInst retInst);

    T visit(Instructions.FPToSIInst inst);

    T visit(Instructions.SIToFPInst inst);

    default T visit(Instruction instruction) { return null; }
}
