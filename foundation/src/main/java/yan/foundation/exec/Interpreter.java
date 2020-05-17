package yan.foundation.exec;

import yan.foundation.ir.Module;
import yan.foundation.ir.*;
import yan.foundation.ir.constant.ConstantArray;
import yan.foundation.ir.constant.ConstantFP;
import yan.foundation.ir.constant.ConstantInt;
import yan.foundation.ir.constant.ConstantStruct;
import yan.foundation.ir.inst.InstVoidVisitor;
import yan.foundation.ir.inst.Instruction;
import yan.foundation.ir.type.IRType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public abstract class Interpreter implements InstVoidVisitor {
    protected Stack<ExecContext> rtStack = new Stack<>();
    protected GenericValue exitValue;

    protected Module module;

    // memory to store global variable
    protected Map<Value, GenericValue> globals;

    public Interpreter(Module module) {
        this.module = module;
        setupGlobals();
    }

    protected void setupGlobals() {
        for (var gvar : module.globals) {
            if (gvar.hasInitializer()) {
                globals.put(gvar, getConstantValue(gvar.getInitializer()));
            } else {
                globals.put(gvar, GenericValue.ZeroInitialized(gvar.getType()));
            }
        }
    }

    public GenericValue runFunction(Function function, List<GenericValue> args) {
        call(function, args);
        run();
        return exitValue;
    }

    // setup call frame, aka stack frame
    protected void call(Function function, List<GenericValue> args) {
        // Create a new frame
        ExecContext stackFrame = new ExecContext(function);

        // Setup non-varargs arguments (bind IRValue with its GenericValue)
        assert function.numOfParameters() == args.size() :
                "Invalid number of values passed to function invocation!";

        for (int i = 0; i < args.size(); i++) {
            setValue(function.parameterAt(i), args.get(i), stackFrame);
        }

        // Push the new frame into stack
        rtStack.push(stackFrame);
    }

    // Start executing instruction
    protected void run() {
        while (!rtStack.empty()) {
            // Get current stack frame
            ExecContext frame = rtStack.peek();

            // Get current instruction
            Instruction inst = frame.pc.next();

            // Dispatch and execute the instruction
            exec(inst);
        }
    }

    protected abstract void exec(Instruction inst);

    // ------------ Useful utils ------------ //

    protected void setValue(Value value, GenericValue bindingValue, ExecContext context) {
        context.values.put(value, bindingValue);
    }

    protected GenericValue getOperandValue(Value value, ExecContext context) {
        if (value instanceof GlobalValue) {
            return getPointerToGlobal((GlobalValue) value);
        } else if (value instanceof Constant) {
            return getConstantValue((Constant) value);
        } else {
            return context.values.get(value);
        }
    }

    private GenericValue getPointerToGlobal(GlobalValue value) {
        if (value instanceof Function) {
            var result = new GenericValue();
            result.function = (Function) value;
            return result;
        } else {
            return globals.get(value);
        }
    }

    private GenericValue getConstantValue(Constant value) {
        var result = new GenericValue();
        if (value instanceof ConstantInt) {
            result.intValue = ((ConstantInt) value).value;
        } else if (value instanceof ConstantFP) {
            var fp = (ConstantFP) value;
            if (value.getType().kind == IRType.Kind.FLOAT) {
                result.floatValue = (float) fp.value;
            } else if (value.getType().kind == IRType.Kind.DOUBLE) {
                result.doubleValue = fp.value;
            } else {
                throw new IllegalStateException("Unhandled type for float point constant: " +
                                                value.getType().kind);
            }
        } else if (value instanceof ConstantStruct) {
            var struct = (ConstantStruct) value;
            result.aggregateValue = new ArrayList<>();
            for (Constant member : struct.members) {
                result.aggregateValue.add(getConstantValue(member));
            }
        } else if (value instanceof ConstantArray) {
            var struct = (ConstantArray) value;
            result.aggregateValue = new ArrayList<>();
            for (Constant element : struct.elements) {
                result.aggregateValue.add(getConstantValue(element));
            }
        } else {
            throw new IllegalStateException("illegal constant type: " +
                                            value.getClass().getSimpleName());
        }
        return result;
    }

    protected ExecContext getCurrentFrame() {
        return rtStack.peek();
    }
}
