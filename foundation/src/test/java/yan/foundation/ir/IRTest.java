package yan.foundation.ir;

import org.junit.jupiter.api.Test;
import yan.foundation.exec.GenericValue;
import yan.foundation.exec.Interpreter;
import yan.foundation.exec.InterpreterImpl;
import yan.foundation.ir.inst.Instructions;
import yan.foundation.ir.type.FloatType;
import yan.foundation.ir.type.FunctionType;
import yan.foundation.ir.type.IntegerType;

import java.util.List;

public class IRTest {
    @Test
    public Module testModule() {
        Module module = new Module("test");
        IRBuilder builder = new IRBuilder(module);

        Function function = builder.addFunction("calculateFibs",
                                                FunctionType.get(FloatType.Double,
                                                                 List.of(IntegerType.int1)));
        BasicBlock entry = function.appendBasicBlock("entry");
        builder.positionAtEnd(entry);

        var local = builder.buildAlloca(FloatType.Double, "local");

        var test = builder.buildICmp(function.parameterAt(0), IntegerType.int1.zero(),
                                     Instructions.CmpInst.Predicate.ICMP_EQ);

        // Create basic blocks for "then", "else", and "merge"
        var thenBB = function.appendBasicBlock("then");
        var elseBB = function.appendBasicBlock("else");
        var mergeBB = function.appendBasicBlock("merge");

        builder.buildCondBr(test, thenBB, elseBB);

        // MARK: Then Block
        builder.positionAtEnd(thenBB);

        // local = 1/89, the fibonacci series (sort of)
        var thenVal = FloatType.Double.constant(1.0 / 89);
        builder.buildStore(local, thenVal);
        builder.buildBr(mergeBB);

        // MARK: Else Block
        builder.positionAtEnd(elseBB);
        // local = 1/109, the fibonacci series (sort of) backwards
        var elseVal = FloatType.Double.constant(1.0 / 109);
        builder.buildStore(local, elseVal);
        builder.buildBr(mergeBB);

        builder.positionAtEnd(mergeBB);
        var ret = builder.buildLoad(local, "ret");
        builder.buildRet(ret);
        return module;
    }

    @Test
    public void testInterpret() {
        Module module = testModule();
        Interpreter interpreter = new InterpreterImpl(module);
        var value = interpreter.runFunction(module.getNamedFunction("calculateFibs"), List.of(GenericValue.Int(1)));
        System.out.println(value.doubleValue);
    }
}
