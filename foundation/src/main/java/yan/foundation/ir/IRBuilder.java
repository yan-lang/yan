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

    public void insert(Instruction inst, String name) {
        insertBlock.instructions.define(name, inst);
    }

    // --------------- Conveniences Instruction --------------- //

    public Value buildBinaryOperation(OpCode.Binary op, Value lhs, Value rhs, String name) {
        return null;
    }

    public Value buildCast(OpCode.Cast op, Value value, IRType type, String name) {
        return null;
    }

    //

    public Function addFunction(String name, FunctionType type) {
        return module.addFunction(name, type);
    }

    // --------------- Arithmetic Instruction --------------- //

    public Instruction buildNeg(Value value, String name) {
        IRType type = value.getType();
        Instruction inst;
        if (type.kind == IRType.Kind.INTEGER) {
            inst = new BinaryOpInst(OpCode.Binary.SUB, value, ((IntegerType) type).constant(0), insertBlock);
        } else if (type.kind == IRType.Kind.FLOAT) {
            inst = new BinaryOpInst(OpCode.Binary.FSUB, value, ((FloatType) type).constant(0), insertBlock);
        } else {
            throw new IllegalStateException("unexpected type for neg instruction");
        }
        insert(inst, name);
        return inst;
    }

    public Instruction buildAdd(Value lhs, Value rhs, String name) {
        return buildBinaryOp(OpCode.Binary.ADD, OpCode.Binary.FADD, lhs, rhs, name);

    }

    public Instruction buildSub(Value lhs, Value rhs, String name) {
        return buildBinaryOp(OpCode.Binary.SUB, OpCode.Binary.FSUB, lhs, rhs, name);
    }

    public Instruction buildMul(Value lhs, Value rhs, String name) {
        return buildBinaryOp(OpCode.Binary.MUL, OpCode.Binary.FMUL, lhs, rhs, name);
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
            inst = new BinaryOpInst(intOp, lhs, rhs, insertBlock);
        } else if (type.kind == IRType.Kind.FLOAT) {
            inst = new BinaryOpInst(floatOp, lhs, rhs, insertBlock);
        } else {
            throw new IllegalStateException("unexpected type for " + intOp + " instruction");
        }
        insert(inst, name);
        return inst;
    }

    public Instruction buildFCmp(Value lhs, Value rhs, CmpInst.Predicate predicate, String name) {
        Instruction inst = new FCmpInst(predicate, lhs, rhs, name, insertBlock);
        insert(inst, "");
        return inst;
    }

    public Instruction buildICmp(Value lhs, Value rhs, CmpInst.Predicate predicate) {
        return buildICmp(lhs, rhs, predicate, "");
    }

    public Instruction buildICmp(Value lhs, Value rhs, CmpInst.Predicate predicate, String name) {
        Instruction inst = new ICmpInst(predicate, lhs, rhs, name, insertBlock);
        insert(inst, "");
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

    public AllocaInst buildAlloca(IRType type, String name) {
        AllocaInst inst = new AllocaInst(type, name, insertBlock);
        insert(inst, name);
        return inst;
    }

    public StoreInst buildStore(Value ptr, Value value) {
        return buildStore(ptr, value, "");
    }

    public StoreInst buildStore(Value ptr, Value value, String name) {
        StoreInst inst = new StoreInst(value, ptr, name, insertBlock);
        insert(inst, name);
        return inst;
    }

    public LoadInst buildLoad(Value value, String name) {
        LoadInst inst = new LoadInst(value, name, insertBlock);
        insert(inst, name);
        return inst;
    }

    public Value buildGEP(Value ptr, Value index, String name) {
        Instruction inst = new GetElementPtrInst(ptr, index, name, insertBlock);
        insert(inst, name);
        return inst;
    }

    // ------------- Terminator instruction ------------- //

    public Instruction buildBr(BasicBlock block) {
        Instruction inst = new BranchInst(block);
        insert(inst, "");
        return inst;
    }

    public Instruction buildCondBr(Value condition, BasicBlock thenBlock, BasicBlock elseBlock) {
        Instruction inst = new BranchInst(thenBlock, elseBlock, condition);
        insert(inst, "");
        return inst;
    }

    public Instruction buildRet(Value value) {
        Instruction inst = new RetInst(value, insertBlock);
        insert(inst, "");
        return inst;
    }

    public Instruction buildRetVoid() {
        Instruction inst = new RetInst(insertBlock);
        insert(inst, "");
        return inst;
    }

    public CallInst buildCall(Value fn, List<Value> args) {
        var type = (FunctionType) ((PointerType) fn.getType()).getElementType();
        CallInst call = new CallInst(type, fn, args, "", insertBlock);
        insert(call, "");
        return call;
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
