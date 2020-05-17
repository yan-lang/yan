package yan.foundation.ir.inst;

public interface InstVoidVisitor {
    void visit(Instructions.BinaryOpInst binaryOpInst);

    void visit(Instructions.AllocaInst allocaInst);

    void visit(Instructions.BranchInst branchInst);

    void visit(Instructions.CallInst callInst);

    void visit(Instructions.ICmpInst iCmpInst);

    void visit(Instructions.FCmpInst fCmpInst);

    void visit(Instructions.GetElementPtrInst getElementPtrInst);

    void visit(Instructions.LoadInst loadInst);

    void visit(Instructions.StoreInst storeInst);

    void visit(Instructions.RetInst retInst);

    default void visit(Instruction instruction) {}
}
