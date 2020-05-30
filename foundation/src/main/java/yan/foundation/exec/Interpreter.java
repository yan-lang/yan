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

import java.util.*;

public abstract class Interpreter implements InstVoidVisitor {
    protected Stack<ExecContext> rtStack = new Stack<>();
    protected GenericValue exitValue;

    protected Module module;

    // memory to store global variable
    protected Map<Value, GenericValue> globals = new HashMap<>();

    protected Map<String, ExternalFunction> externalFunctions = new HashMap<>();

    public Interpreter(Module module) {
        this.module = module;
        setupGlobals();
        initializeExternalFunctions();
    }

    protected void initializeExternalFunctions() {
        externalFunctions.put("yil.readInt", ExternalFunction.readInt());
        externalFunctions.put("yil.printInt", ExternalFunction.printInt());
    }

    protected void setupGlobals() {
        for (var gvar : module.globals) {
            if (gvar.hasInitializer()) {
                var value = new GenericValue();
                value.pointerValue = getConstantValue(gvar.getInitializer());
                globals.put(gvar, value);
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
        ExecContext stackFrame = new ExecContext();
        stackFrame.currentFunction = function;

        // Push the new frame into stack
        rtStack.push(stackFrame);

        // Special handling for external functions (Intrinsic actually for now)
        if (function.isDeclaration()) {
            GenericValue Result = callExternalFunction(function, args);
            // Simulate a 'ret' instruction of the appropriate type.
            popStackAndReturnValueToCaller(function.getFunctionType().getReturnType(), Result);
            return;
        }

        // Setup program counter
        stackFrame.currentBlock = function.entryBlock();
        stackFrame.pc = function.entryBlock().iterator();

        // Setup non-varargs arguments (bind IRValue with its GenericValue)
        assert function.numOfParameters() == args.size() :
                "Invalid number of values passed to function invocation!";

        for (int i = 0; i < args.size(); i++) {
            setValue(function.parameterAt(i), args.get(i), stackFrame);
        }
    }

    private GenericValue callExternalFunction(Function function, List<GenericValue> args) {
        var F = externalFunctions.get(function.getName());
        assert F != null : "could not find the definition of function " + function.getName();
        return F.call(args);
    }

    protected void popStackAndReturnValueToCaller(IRType type, GenericValue result) {
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
        if (value instanceof Constant) {
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
        if (value instanceof GlobalValue) {
            result = getPointerToGlobal((GlobalValue) value);
        } else if (value instanceof ConstantInt) {
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
