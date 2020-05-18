package yan.foundation.ir;

import yan.foundation.ir.inst.Instruction;
import yan.foundation.ir.inst.Instructions.*;
import yan.foundation.ir.type.*;

import java.util.List;


public class IRBuilder {
    public final Module module;
    protected Function insertFunction;
    protected BasicBlock insertBlock;  // the block that instruction will insert to


    public IRBuilder(Module module) {
        this.module = module;
    }

    // --------------- IR Navigation --------------- //

    public void positionAtEnd(BasicBlock block) {
        insertBlock = block;
    }

//    public void positionBefore(Instruction inst) {
//        insertBlock = inst.getParent();
//        // position instruction insert point
//
//    }

    public Function getCurrentFunction() { return insertFunction; }

    public BasicBlock getCurrentBlock() { return insertBlock; }

    public <T extends Instruction> T insert(T inst) {
        insertBlock.instructions.add(inst);
        return inst;
    }

    // --------------- Conveniences Instruction --------------- //

//    public Value buildBinaryOperation(OpCode.Binary op, Value lhs, Value rhs, String name) {
//        return null;
//    }
//
//    public Value buildCast(OpCode.Cast op, Value value, IRType type, String name) {
//        return null;
//    }

    //

    public Function addFunction(String name, FunctionType type) {
        return module.addFunction(name, type);
    }

    // --------------- Arithmetic Instruction --------------- //

    public Instruction buildNeg(Value value) {
        return buildNeg(value, "");
    }

    public Instruction buildNeg(Value value, String name) {
        IRType type = value.getType();
        Instruction inst;
        if (type.kind == IRType.Kind.INTEGER) {
            inst = new BinaryOpInst(OpCode.Binary.SUB, value, ((IntegerType) type).constant(0),
                                    name, insertBlock);
        } else if (type.kind == IRType.Kind.FLOAT) {
            inst = new BinaryOpInst(OpCode.Binary.FSUB, value, ((FloatType) type).constant(0),
                                    name, insertBlock);
        } else {
            throw new IllegalStateException("unexpected type for neg instruction");
        }
        return insert(inst);
    }

    public Instruction buildAdd(Value lhs, Value rhs) {
        return buildAdd(lhs, rhs, "");
    }

    public Instruction buildAdd(Value lhs, Value rhs, String name) {
        return buildBinaryOp(OpCode.Binary.ADD, OpCode.Binary.FADD, lhs, rhs, name);
    }

    public Instruction buildSub(Value lhs, Value rhs) {
        return buildSub(lhs, rhs, "");
    }

    public Instruction buildSub(Value lhs, Value rhs, String name) {
        return buildBinaryOp(OpCode.Binary.SUB, OpCode.Binary.FSUB, lhs, rhs, name);
    }

    public Instruction buildMul(Value lhs, Value rhs) {
        return buildMul(lhs, rhs, "");
    }

    public Instruction buildMul(Value lhs, Value rhs, String name) {
        return buildBinaryOp(OpCode.Binary.MUL, OpCode.Binary.FMUL, lhs, rhs, name);
    }

    public Instruction buildDiv(Value lhs, Value rhs) {
        return buildDiv(lhs, rhs, "");
    }

    public Instruction buildDiv(Value lhs, Value rhs, String name) {
        return buildBinaryOp(OpCode.Binary.DIV, OpCode.Binary.FDIV, lhs, rhs, name);
    }

    public Instruction buildRem(Value lhs, Value rhs, String name) {
        throw new IllegalStateException("not implemented");
    }

    private Instruction buildBinaryOp(OpCode.Binary intOp, OpCode.Binary floatOp, Value lhs, Value rhs, String name) {
        IRType type = lhs.getType();
        Instruction inst;
        if (type.kind == IRType.Kind.INTEGER) {
            inst = new BinaryOpInst(intOp, lhs, rhs, name, insertBlock);
        } else if (type.kind == IRType.Kind.FLOAT) {
            inst = new BinaryOpInst(floatOp, lhs, rhs, name, insertBlock);
        } else {
            throw new IllegalStateException("unexpected type for " + intOp + " instruction");
        }
        return insert(inst);
    }

    public Instruction buildEQ(Value lhs, Value rhs) {
        return buildCmpInst(CmpInst.Predicate.ICMP_EQ, CmpInst.Predicate.FCMP_EQ, lhs, rhs, "==");
    }

    public Instruction buildNEQ(Value lhs, Value rhs) {
        return buildCmpInst(CmpInst.Predicate.ICMP_NEQ, CmpInst.Predicate.FCMP_NEQ, lhs, rhs, "!=");
    }

    public Instruction buildGT(Value lhs, Value rhs) {
        return buildCmpInst(CmpInst.Predicate.ICMP_GT, CmpInst.Predicate.FCMP_GT, lhs, rhs, ">");
    }

    public Instruction buildGTE(Value lhs, Value rhs) {
        return buildCmpInst(CmpInst.Predicate.ICMP_GTE, CmpInst.Predicate.FCMP_GTE, lhs, rhs, ">=");
    }

    public Instruction buildLT(Value lhs, Value rhs) {
        return buildCmpInst(CmpInst.Predicate.ICMP_LT, CmpInst.Predicate.FCMP_LT, lhs, rhs, "<");
    }

    public Instruction buildLTE(Value lhs, Value rhs) {
        return buildCmpInst(CmpInst.Predicate.ICMP_LTE, CmpInst.Predicate.FCMP_LTE, lhs, rhs, "<=");
    }

    private Instruction buildCmpInst(CmpInst.Predicate intOp, CmpInst.Predicate floatOp, Value lhs, Value rhs, String name) {
        if (lhs.getType().isFloatingPointType()) {
            return buildFCmp(lhs, rhs, floatOp);
        } else if (lhs.getType().isIntegerType()) {
            return buildICmp(lhs, rhs, intOp);
        } else {
            throw new IllegalStateException("unexpected type for " + name + " instruction");
        }
    }

    public Instruction buildFCmp(Value lhs, Value rhs, CmpInst.Predicate predicate) {
        return buildFCmp(lhs, rhs, predicate, "");
    }

    public Instruction buildFCmp(Value lhs, Value rhs, CmpInst.Predicate predicate, String name) {
        Instruction inst = new FCmpInst(predicate, lhs, rhs, name, insertBlock);
        insert(inst);
        return inst;
    }

    public Instruction buildICmp(Value lhs, Value rhs, CmpInst.Predicate predicate) {
        return buildICmp(lhs, rhs, predicate, "");
    }

    public Instruction buildICmp(Value lhs, Value rhs, CmpInst.Predicate predicate, String name) {
        Instruction inst = new ICmpInst(predicate, lhs, rhs, name, insertBlock);
        insert(inst);
        return inst;
    }

    // --------------- Logical Instruction --------------- //

    public Instruction buildNot(Value value, String name) {
        if (value.getType().kind != IRType.Kind.INTEGER) {
            throw new IllegalStateException("not instruction only handle integer operands");
        }
        return null;
    }

    public Instruction buildAnd(Value lhs, Value rhs, String name) {
        return null;
    }

    public Instruction buildXor(Value lhs, Value rhs, String name) {
        return null;
    }

    public Instruction buildOr(Value lhs, Value rhs, String name) {
        return null;
    }

    public Instruction buildShl(Value lhs, Value rhs, String name) {
        return null;
    }

    public Instruction buildShr(Value lhs, Value rhs, String name) {
        return buildShr(lhs, rhs, false, name);
    }

    public Instruction buildShr(Value lhs, Value rhs, boolean isArithmetic, String name) {
        return null;
    }

    // --------------- Memory Instruction --------------- //

    public AllocaInst buildAlloca(IRType type) {
        return buildAlloca(type, "");
    }

    public AllocaInst buildAlloca(IRType type, String name) {
        AllocaInst inst = new AllocaInst(type, name, insertBlock);
        return insert(inst);
    }

    public StoreInst buildStore(Value ptr, Value value) {
        return buildStore(ptr, value, "");
    }

    public StoreInst buildStore(Value ptr, Value value, String name) {
        StoreInst inst = new StoreInst(value, ptr, name, insertBlock);
        return insert(inst);
    }

    public LoadInst buildLoad(Value value) {
        return buildLoad(value, "");
    }

    public LoadInst buildLoad(Value value, String name) {
        LoadInst inst = new LoadInst(value, name, insertBlock);
        return insert(inst);
    }

    public Value buildGEP(Value ptr, Value index) {
        return buildGEP(ptr, index, "");
    }

    public Value buildGEP(Value ptr, Value index, String name) {
        Instruction inst = new GetElementPtrInst(ptr, index, name, insertBlock);
        return insert(inst);
    }

    // ------------- Terminator instruction ------------- //

    public Instruction buildBr(BasicBlock block) {
        Instruction inst = new BranchInst(block);
        return insert(inst);
    }

    public Instruction buildCondBr(Value condition, BasicBlock thenBlock, BasicBlock elseBlock) {
        Instruction inst = new BranchInst(thenBlock, elseBlock, condition);
        return insert(inst);
    }

    public Instruction buildRet(Value value) {
        Instruction inst = new RetInst(value, insertBlock);
        return insert(inst);
    }

    public Instruction buildRetVoid() {
        Instruction inst = new RetInst(insertBlock);
        return insert(inst);
    }

    public CallInst buildCall(Value fn, List<Value> args) {
        return buildCall(fn, args, "");
    }

    public CallInst buildCall(Value fn, List<Value> args, String name) {
        var type = (FunctionType) ((PointerType) fn.getType()).getElementType();
        CallInst call = new CallInst(type, fn, args, name, insertBlock);
        return insert(call);
    }


    // ------------- Global Variable instruction ------------- //

    public GlobalVariable addGlobalVariable(String name, IRType type) {
        return module.addGlobalVariable(name, type);
    }

    public GlobalVariable addGlobalVariable(String name, Constant initializer) {
        return module.addGlobalVariable(name, initializer);
    }

    public GlobalVariable addGlobalString(String name, String value) {
        return module.addGlobalString(name, value);
    }
}
