package yan.foundation.ir;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import yan.foundation.exec.GenericValue;
import yan.foundation.exec.Interpreter;
import yan.foundation.exec.InterpreterImpl;
import yan.foundation.ir.constant.ConstantStruct;
import yan.foundation.ir.inst.Instructions;
import yan.foundation.ir.type.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.function.Consumer;

public class IRTest {
    private final InputStream systemIn = System.in;

    private void provideInput(String data) {
        ByteArrayInputStream testIn = new ByteArrayInputStream(data.getBytes());
        System.setIn(testIn);
    }

    @AfterEach
    public void restoreSystemInputOutput() {
        System.setIn(systemIn);
    }


    @Test
    public void testModule() {
        Module module = new Module("test");
        IRBuilder builder = new IRBuilder(module);

        Function function = builder.addFunction("calculateFibs",
                                                FunctionType.get(FloatType.Double,
                                                                 List.of(IntegerType.int32)));
        BasicBlock entry = function.appendBasicBlock("entry");
        builder.positionAtEnd(entry);

        var local = builder.buildAlloca(FloatType.Double, "local");

        var test = builder.buildICmp(function.parameterAt(0), IntegerType.int32.zero(),
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

        Interpreter interpreter = new InterpreterImpl(module);
        var value = interpreter.runFunction(module.getNamedFunction("calculateFibs"), List.of(GenericValue.Int(1)));
        System.out.println(value.doubleValue);
    }

    public void testGlobal() {
        /*
         * int question=10;
         *
         * int f(int x) {
         *  if(x <= 2) return 1;
         *  else return f(x-1) + f(x-2);
         * }
         *
         * int main() {
         *  return f(question);
         * }
         */
        Module module = new Module("testGlobal");
        IRBuilder builder = new IRBuilder(module);

        var gvarQuestion = builder.addGlobalVariable("question", IntegerType.int32.constant(10));

        var type = FunctionType.get(IntegerType.int32, List.of(IntegerType.int32));
        Function function = builder.addFunction("f", type);

        var entry = function.appendBasicBlock("entry");
        builder.positionAtEnd(entry);

        var test = builder.buildICmp(function.parameterAt(0),
                                     IntegerType.int32.constant(2),
                                     Instructions.CmpInst.Predicate.ICMP_LTE);

        var thenBlock = function.appendBasicBlock("if.then");
        var elseBlock = function.appendBasicBlock("if.else");

        builder.buildCondBr(test, thenBlock, elseBlock);

        builder.positionAtEnd(thenBlock);
        builder.buildRet(IntegerType.int32.constant(1));

        builder.positionAtEnd(elseBlock);

        var arg1 = builder.buildSub(function.parameterAt(0),
                                    IntegerType.int32.constant(1),
                                    "arg1");
        var tmp1 = builder.buildCall(function, List.of(arg1));

        var arg2 = builder.buildSub(function.parameterAt(0),
                                    IntegerType.int32.constant(2),
                                    "arg2");
        var tmp2 = builder.buildCall(function, List.of(arg2));

        var tmp3 = builder.buildAdd(tmp1, tmp2, "result");

        builder.buildRet(tmp3);

        var main = builder.addFunction("main", FunctionType.get(IntegerType.int32));
        var entry2 = main.appendBasicBlock("entry");

        builder.positionAtEnd(entry2);

//        var question = builder.buildLoad(gvarQuestion, "ques");
        var question = builder.buildCallReadInt();

        var tmp5 = builder.buildCall(function, List.of(question));

        builder.buildCallPrintInt(tmp5);
        builder.buildRet(tmp5);

        int input = 10;
        provideInput(Integer.toString(input));

        Interpreter interpreter = new InterpreterImpl(module);
        var value = interpreter.runFunction(module.getNamedFunction("main"), List.of());
        Assertions.assertEquals(55, value.intValue);

//        return module;
    }

    @Test
    public Module testVTable() {
        Module module = new Module("testVTable");
        IRBuilder builder = new IRBuilder(module);

        /*
        T.Vtable { int32 (int32) }
         */
        var fType = FunctionType.get(IntegerType.int32, List.of(IntegerType.int32));
        var vtableType = StructType.get("T.Vtable", List.of(PointerType.get(fType)));
        var vtable = builder.addGlobalVariable("vtable", vtableType);

        var test = builder.addFunction("test", fType);
        var entry = test.appendBasicBlock("entry");
        builder.positionAtEnd(entry);
        var ret = builder.buildMul(test.parameterAt(0), IntegerType.int32.constant(2), "");
        builder.buildRet(ret);

        vtable.setInitializer(new ConstantStruct(vtableType, List.of(test)));

        var main = builder.addFunction("main", FunctionType.get(IntegerType.int32));
        var entry2 = main.appendBasicBlock("entry");
        builder.positionAtEnd(entry2);
        var fp = builder.buildGEP(vtable, IntegerType.int32.constant(0), "");
        var f = builder.buildLoad(fp, "f");
        var ret2 = builder.buildCall(f, List.of(IntegerType.int32.constant(10)));
        builder.buildRet(ret2);

        Interpreter interpreter = new InterpreterImpl(module);
        var value = interpreter.runFunction(module.getNamedFunction("main"), List.of());
        System.out.println(value.intValue);
        return module;
    }

    @Test
    public void testRem() {
        Module module = new Module("testRem");
        IRBuilder builder = new IRBuilder(module);

        var main = builder.addFunction("main", FunctionType.get(VoidType.get()));
        var entry = main.appendBasicBlock("entry");
        builder.positionAtEnd(entry);
        var ret = builder.buildRem(IntegerType.int32.constant(28), IntegerType.int32.constant(6));
        builder.buildRet(ret);

        Interpreter interpreter = new InterpreterImpl(module);
        var value = interpreter.runFunction(module.getNamedFunction("main"), List.of());
        System.out.println(value.intValue);
    }

    @Test
    public void testDump() {
        Module module = testVTable();
        System.out.println(module.dump());
    }

    @Test
    public void testAnd() {
        Module module = buildModule((builder) -> {
            // 9: 1001, 5:0101
            var and = builder.buildAnd(IntegerType.int32.constant(9), IntegerType.int32.constant(5), "");
            builder.buildRet(and);
        });

        Interpreter interpreter = new InterpreterImpl(module);
        var value = interpreter.runFunction(module.getNamedFunction("main"), List.of());
        System.out.println(value.intValue);
        Assertions.assertEquals(1, value.intValue);
    }

    @Test
    public void testOr() {
        Module module = buildModule((builder) -> {
            // 9: 1001, 5:0101
            var and = builder.buildOr(IntegerType.int32.constant(9), IntegerType.int32.constant(5), "");
            builder.buildRet(and);
        });

        Interpreter interpreter = new InterpreterImpl(module);
        var value = interpreter.runFunction(module.getNamedFunction("main"), List.of());
        System.out.println(value.intValue);
        Assertions.assertEquals(0b1101, value.intValue);
    }

    @Test
    public void testXOr() {
        Module module = buildModule((builder) -> {
            // 9: 1001, 5:0101
            var and = builder.buildXor(IntegerType.int32.constant(9), IntegerType.int32.constant(5), "");
            builder.buildRet(and);
        });

        Interpreter interpreter = new InterpreterImpl(module);
        var value = interpreter.runFunction(module.getNamedFunction("main"), List.of());
        System.out.println(value.intValue);
        Assertions.assertEquals(0b1100, value.intValue);
    }

    @Test
    public void testShl() {
        Module module = buildModule((builder) -> {
            // 9: 1001, 5:0101
            var and = builder.buildShl(IntegerType.int32.constant(9), IntegerType.int32.constant(5), "");
            builder.buildRet(and);
        });

        Interpreter interpreter = new InterpreterImpl(module);
        var value = interpreter.runFunction(module.getNamedFunction("main"), List.of());
        System.out.println(value.intValue);
        Assertions.assertEquals(0b100100000, value.intValue);
    }

    @Test
    public void testAShr() {
        Module module = buildModule((builder) -> {
            // 9: 1001, -9: 11..110111, -9>>2: 11..1101 = 0011
            var and = builder.buildShr(IntegerType.int32.constant(-9),
                                       IntegerType.int32.constant(2),
                                       true,
                                       "");
            builder.buildRet(and);
        });

        Interpreter interpreter = new InterpreterImpl(module);
        var value = interpreter.runFunction(module.getNamedFunction("main"), List.of());
        System.out.println(value.intValue);
        Assertions.assertEquals(-3, value.intValue);
    }

    @Test
    public void testLShr() {
        Module module = buildModule((builder) -> {
            var and = builder.buildShr(IntegerType.int32.constant(-9),
                                       IntegerType.int32.constant(2),
                                       false,
                                       "");
            builder.buildRet(and);
        });

        Interpreter interpreter = new InterpreterImpl(module);
        var value = interpreter.runFunction(module.getNamedFunction("main"), List.of());
        System.out.println(value.intValue);
        Assertions.assertEquals(1073741821, value.intValue);
    }

    private Module buildModule(Consumer<IRBuilder> body) {
        Module module = new Module("test");
        IRBuilder builder = new IRBuilder(module);

        var main = builder.addFunction("main", FunctionType.get(VoidType.get()));
        var entry = main.appendBasicBlock("entry");
        builder.positionAtEnd(entry);

        body.accept(builder);

        return module;
    }
}
