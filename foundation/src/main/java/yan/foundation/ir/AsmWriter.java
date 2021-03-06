package yan.foundation.ir;

import yan.foundation.ir.inst.InstVisitor;
import yan.foundation.ir.inst.Instruction;
import yan.foundation.ir.inst.Instructions;
import yan.foundation.ir.type.StructType;
import yan.foundation.utils.printer.IndentPrinter;

import java.util.stream.Collectors;

public class AsmWriter {
    private final Module module;
    private final IndentPrinter printer = new IndentPrinter();
    private final InstructionWriter instructionWriter = new InstructionWriter();
    private String asm;

    public AsmWriter(Module module) {
        this.module = module;
    }

    public String dump() {
        if (asm == null) build();
        return asm;
    }

    private void build() {
        buildModulePrologue();
        buildTypes();
        buildGlobals();
        buildFunctions();
        asm = printer.flush();
    }

    private void buildModulePrologue() {
        printer.print("module: ").println(module.moduleID);
        printer.println();
    }

    private void buildTypes() {
        printer.println("types {");
        printer.indent();
        for (var type : StructType.namedTypes.values()) {
            printer.print(type.getName()).print(" = ").print(type.toStructString());
            printer.println();
        }
        printer.unindent();
        printer.println("}");
        printer.println();
    }

    private void buildGlobals() {
        printer.println("globals {");
        printer.indent();
        for (var gvar : module.globals) {
            printer.print("@" + gvar.getName())
                   .print(": ")
                   .print(gvar.getType().getElementType().toString());
            if (gvar.hasInitializer()) {
                printer.print(" = ").print(gvar.getInitializer().toString());
            }
            printer.println();
        }
        printer.unindent();
        printer.println("}");
        printer.println();
    }

    private void buildFunctions() {
        for (Function f : module) {
            buildFunction(f);
            printer.println();
        }
    }

    private void buildFunction(Function f) {
        // emit function prototype
        // def retType @name (signature) { body }
        var params = f.getFunctionType().getParamTypes().stream()
                      .map(Object::toString).collect(Collectors.joining(", "));
        printer.print("def ")
               .print(f.getFunctionType().getReturnType().toString())
               .print(" ").print("@").print(f.name)
               .print("(").print(params).print(")")
               .println("{");

        printer.indent();

        // build basic blocks
        for (BasicBlock block : f) {
            buildBasicBlock(block);
        }

        printer.unindent();
        printer.println("}");
    }

    private void buildBasicBlock(BasicBlock block) {
        printer.unindent();
        printer.println(block.name + ":");
        printer.indent();

        for (Instruction inst : block) {
            printer.println(inst.accept(instructionWriter));
        }
    }

    static class InstructionWriter implements InstVisitor<String> {

        @Override
        public String visit(Instructions.BinaryOpInst inst) {
            String op = inst.op.toString().toLowerCase();
            return String.format("%s = %s %s, %s", inst, op,
                                 inst.getLHSOperand(),
                                 inst.getRHSOperand());
        }

        @Override
        public String visit(Instructions.AllocaInst inst) {
            return String.format("%s = alloca %s", inst, inst.getAllocatedType());
        }

        @Override
        public String visit(Instructions.BranchInst inst) {
            if (inst.isConditional()) return String.format("br %s, %s, %s",
                                                           inst.getCondition(),
                                                           inst.getSuccessor(0),
                                                           inst.getSuccessor(1));
            else return String.format("br %s", inst.getSuccessor(0));
        }

        @Override
        public String visit(Instructions.CallInst callInst) {
            return String.format("%s = call %s(%s)",
                                 callInst,
                                 callInst.getCalledOperand(),
                                 callInst.getArgOperands().stream()
                                         .map(Value::toString)
                                         .collect(Collectors.joining(", ")));
        }

        @Override
        public String visit(Instructions.ICmpInst inst) {
            return String.format("%s = icmp %s %s, %s", inst,
                                 inst.pred.toString().substring(5).toLowerCase(),
                                 inst.getOperand(0),
                                 inst.getOperand(1));
        }

        @Override
        public String visit(Instructions.FCmpInst inst) {
            return String.format("%s = fcmp %s %s, %s", inst,
                                 inst.pred.toString().toLowerCase(),
                                 inst.getOperand(0),
                                 inst.getOperand(1));
        }

        @Override
        public String visit(Instructions.GetElementPtrInst inst) {
            return String.format("%s = getelementptr %s, %s", inst,
                                 inst.getPointerOperand(),
                                 inst.getIndexOperand());
        }

        @Override
        public String visit(Instructions.LoadInst loadInst) {
            return loadInst + " = load " + loadInst.getUnaryOperand();
        }

        @Override
        public String visit(Instructions.StoreInst storeInst) {
            return "store " + storeInst.getAddr() + ", " + storeInst.getValue();
        }

        @Override
        public String visit(Instructions.RetInst retInst) {
            if (retInst.isVoidRet()) return "ret";
            return "ret " + retInst.getReturnValue();
        }

        @Override
        public String visit(Instructions.FPToSIInst inst) {
            return inst + " = fptosi " + inst.getUnaryOperand();
        }

        @Override
        public String visit(Instructions.SIToFPInst inst) {
            return inst + " = sitofp " + inst.getUnaryOperand();
        }
    }
}
