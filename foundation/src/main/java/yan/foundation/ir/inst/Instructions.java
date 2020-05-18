package yan.foundation.ir.inst;

import yan.foundation.ir.BasicBlock;
import yan.foundation.ir.Function;
import yan.foundation.ir.OpCode;
import yan.foundation.ir.Value;
import yan.foundation.ir.constant.ConstantInt;
import yan.foundation.ir.type.*;

import java.util.List;

public class Instructions {

    public static abstract class UnaryInst extends Instruction {
        public UnaryInst(IRType type, Value value, BasicBlock parent) {
            super(type, 1, parent);
            setOperand(0, value);
        }

        public Value getUnaryOperand() {
            return getOperand(0);
        }
    }

    public static abstract class BinaryInst extends Instruction {
        public BinaryInst(IRType type, Value lhs, Value rhs, BasicBlock parent) {
            super(type, 2, parent);
            setOperand(0, lhs);
            setOperand(1, rhs);
        }

        public Value getLHSOperand() { return getOperand(0); }

        public Value getRHSOperand() { return getOperand(1); }
    }

    public static class BinaryOpInst extends BinaryInst {
        public final OpCode.Binary op;

        public BinaryOpInst(OpCode.Binary op, Value lhs, Value rhs, String name, BasicBlock parent) {
            super(lhs.getType(), lhs, rhs, parent);
            this.op = op;
            setName(name);
        }

        public <T> T accept(InstVisitor<T> visitor) {
            return visitor.visit(this);
        }

        public void accept(InstVoidVisitor visitor) {
            visitor.visit(this);
        }
    }

    public static class AllocaInst extends Instruction {
        protected IRType allocatedType;

        public AllocaInst(IRType type, String name, BasicBlock parent) {
            super(PointerType.get(type), 0, parent);
            allocatedType = type;
            setName(name);

            assert type != VoidType.get() : "Cannot allocate void!";
        }

        public IRType getAllocatedType() {
            return allocatedType;
        }

        public <T> T accept(InstVisitor<T> visitor) {
            return visitor.visit(this);
        }

        public void accept(InstVoidVisitor visitor) {
            visitor.visit(this);
        }
    }

    public static class BranchInst extends Instruction {

        public BranchInst(BasicBlock target) {
            super(VoidType.get(), 1, null);
            setOperand(0, target);
        }

        public BranchInst(BasicBlock ifTrue, BasicBlock ifFalse, Value cond) {
            super(VoidType.get(), 3, null);
            setOperand(0, ifTrue);
            setOperand(1, ifFalse);
            setOperand(2, cond);
        }

        public boolean isUnConditional() { return numOfOperands() == 1; }

        public boolean isConditional() { return numOfOperands() == 3; }

        public Value getCondition() {
            assert isConditional() : "Cannot get condition of an unconditional branch!";
            return getOperand(2);
        }

        public void setCondition(Value cond) {
            assert isConditional() : "Cannot set condition of an unconditional branch!";
            setOperand(2, cond);
        }

        public int getNumOfSuccessors() { return isConditional() ? 2 : 1;}

        public BasicBlock getSuccessor(int index) {
            assert index < getNumOfSuccessors() : String.format("Successor %d out of range for Branch!", index);
            return (BasicBlock) operands[index];
        }

        public <T> T accept(InstVisitor<T> visitor) {
            return visitor.visit(this);
        }

        public void accept(InstVoidVisitor visitor) {
            visitor.visit(this);
        }
    }

    public static class CallInst extends Instruction {
        public final FunctionType funcType;

        public CallInst(FunctionType type, Value callee, List<Value> args, String name, BasicBlock parent) {
            super(type.getReturnType(), args.size() + 1, parent);
            this.funcType = type;
            setOperand(0, callee);
            for (int i = 0; i < args.size(); i++) setOperand(i + 1, args.get(i));
            setName(name);
        }

        public boolean isIndirectCall() {
            return !(getCalledOperand() instanceof Function);
        }

        public Value getCalledOperand() {
            return getOperand(0);
        }

        public List<Value> getArgOperands() {
            var args = List.of(operands);
            return args.subList(1, args.size());
        }

        public Value getArgOperand(int index) {
            return getOperand(index + 1);
        }

        public int getNumOfArgs() { return numOfOperands() - 1;}

        public <T> T accept(InstVisitor<T> visitor) {
            return visitor.visit(this);
        }

        public void accept(InstVoidVisitor visitor) {
            visitor.visit(this);
        }
    }

    public abstract static class CmpInst extends Instruction {
        public enum Predicate {
            FCMP_EQ, FCMP_GT, FCMP_GTE, FCMP_LT, FCMP_LTE, FCMP_NEQ,
            ICMP_EQ, ICMP_GT, ICMP_GTE, ICMP_LT, ICMP_LTE, ICMP_NEQ,
        }

        public final Predicate pred;

        public CmpInst(Predicate pred, Value lhs, Value rhs, String name, BasicBlock parent) {
            super(IntegerType.int1, 2, parent);
            this.pred = pred;
            setName(name);
            setOperands(lhs, rhs);
        }

        public boolean isIntPredicate() {
            return pred.ordinal() >= Predicate.ICMP_EQ.ordinal() &&
                   pred.ordinal() <= Predicate.ICMP_NEQ.ordinal();
        }

        public boolean isFloatPredicate() {
            return pred.ordinal() >= Predicate.FCMP_EQ.ordinal() &&
                   pred.ordinal() <= Predicate.FCMP_NEQ.ordinal();
        }

    }

    public static class ICmpInst extends CmpInst {
        public ICmpInst(Predicate pred, Value lhs, Value rhs, String name, BasicBlock parent) {
            super(pred, lhs, rhs, name, parent);
            assertOK();
        }

        private void assertOK() {
            assert isIntPredicate() : "Invalid ICmp predicate value";
            assert getOperand(0).getType() == getOperand(1).getType() :
                    "Both operands to ICmp instruction are not of the same type!";
            assert getOperand(0).getType().isIntegerType() ||
                   getOperand(0).getType().isPointerType() :
                    "Invalid operand types for ICmp instruction";
        }

        public <T> T accept(InstVisitor<T> visitor) {
            return visitor.visit(this);
        }

        public void accept(InstVoidVisitor visitor) {
            visitor.visit(this);
        }
    }

    public static class FCmpInst extends CmpInst {
        public FCmpInst(Predicate pred, Value lhs, Value rhs, String name, BasicBlock parent) {
            super(pred, lhs, rhs, name, parent);
            assertOK();
        }

        private void assertOK() {
            assert isFloatPredicate() : "Invalid FCmp predicate value";
            assert getOperand(0).getType() == getOperand(1).getType() :
                    "Both operands to FCmp instruction are not of the same type!";
            assert getOperand(0).getType().isFloatingPointType() :
                    "Invalid operand types for FCmp instruction";
        }

        public <T> T accept(InstVisitor<T> visitor) {
            return visitor.visit(this);
        }

        public void accept(InstVoidVisitor visitor) {
            visitor.visit(this);
        }
    }

    public static class GetElementPtrInst extends Instruction {
        public final IRType sourceType;

        public GetElementPtrInst(Value pointer, Value index, String name, BasicBlock parent) {
            super(getGEPReturnType(pointer, index), 2, parent);
            this.sourceType = index.getType();
            setOperands(pointer, index);
            setName(name);
        }

        public Value getPointerOperand() { return getOperand(0); }

        public Value getIndexOperand() { return getOperand(1); }

        private static IRType getGEPReturnType(Value pointer, Value index) {
            assert pointer.getType().isPointerType();
            PointerType pointerType = (PointerType) pointer.getType();
            assert pointerType.getElementType().isArrayType() || pointerType.getElementType().isStructType() :
                    "Invalid operand type for gep instruction";
            if (pointerType.getElementType().isStructType()) {
                assert index instanceof ConstantInt : "Index of GEP from a struct must be a int constant";
                ConstantInt indexConst = (ConstantInt) index;
                StructType eType = (StructType) pointerType.getElementType();
                assert indexConst.value < eType.numOfElementTypes() : "Index out of bound for GEP from a struct";
                return PointerType.get(eType.getElementTypeAt(indexConst.value));
            } else {
                ArrayType arrayType = (ArrayType) pointerType.getElementType();
                return PointerType.get(arrayType.getElementType());
            }

        }

        public <T> T accept(InstVisitor<T> visitor) {
            return visitor.visit(this);
        }

        public void accept(InstVoidVisitor visitor) {
            visitor.visit(this);
        }
    }

    public static class LoadInst extends UnaryInst {
        public LoadInst(Value pointer, String name, BasicBlock parent) {
            super(((PointerType) pointer.getType()).getElementType(), pointer, parent);
            setName(name);
        }

        public <T> T accept(InstVisitor<T> visitor) {
            return visitor.visit(this);
        }

        public void accept(InstVoidVisitor visitor) {
            visitor.visit(this);
        }
    }

    public static class StoreInst extends Instruction {
        public StoreInst(Value value, Value addr, String name, BasicBlock parent) {
            super(VoidType.get(), 2, parent);
            setOperands(value, addr);
            setName(name);
            assertOK();
        }

        private void assertOK() {
            assert getValue() != null && getAddr() != null :
                    "Both operands must be non-null!";
            assert getAddr().getType().isPointerType() : "addr must be pointer type";
            assert getValue().getType() == ((PointerType) getAddr().getType()).getElementType() :
                    "addr must be a pointer to value type!";
        }

        public Value getValue() { return getOperand(0); }

        public Value getAddr() { return getOperand(1); }

        public <T> T accept(InstVisitor<T> visitor) {
            return visitor.visit(this);
        }

        public void accept(InstVoidVisitor visitor) {
            visitor.visit(this);
        }
    }

    public static class RetInst extends Instruction {
        public RetInst(Value retVal, BasicBlock parent) {
            super(VoidType.get(), 1, parent);
            setOperand(0, retVal);
        }

        public RetInst(BasicBlock parent) {
            super(VoidType.get(), 0, parent);
        }

        public Value getReturnValue() { return getOperand(0); }

        public boolean isVoidRet() { return numOfOperands() == 0; }

        public <T> T accept(InstVisitor<T> visitor) {
            return visitor.visit(this);
        }

        public void accept(InstVoidVisitor visitor) {
            visitor.visit(this);
        }
    }

}
