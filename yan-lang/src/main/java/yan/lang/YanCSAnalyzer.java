package yan.lang;

import yan.lang.predefine.YanTree;
import yan.lang.predefine.abc.AbstractControlStructureAnalyzer;

import java.util.Stack;

/**
 * {@code YanCSAnalyzer}负责部分控制流相关的语义分析
 * return:
 * 1. 必须在函数体内, 不在则报错, 记录return对应的函数
 * <p>
 * break, continue:
 * 1. 必须在循环体内, 不在则报错, 记录对应的循环
 */
public class YanCSAnalyzer extends AbstractControlStructureAnalyzer {
    // 在Yan中函数是不可以嵌套的
    YanTree.FuncDef currentFunc;

    // 循环可以嵌套, 因此需要用栈
    Stack<YanTree.While> loopStack = new Stack<>();

    @Override
    public YanTree.Program transform(YanTree.Program input) {
        YanTree.Program program = super.transform(input);
        loopStack.clear();
        return program;
    }

    @Override
    public void visit(YanTree.FuncDef that) {
        currentFunc = that;
        that.body.accept(this);
        currentFunc = null;
    }

    @Override
    public void visit(YanTree.Return that) {
        that.func = currentFunc;
        if (that.func == null)
            logger.log(Errors.ReturnNotInFunction(that));
    }

    @Override
    public void visit(YanTree.While that) {
        loopStack.push(that);
        that.body.accept(this);
        loopStack.pop();
    }

    @Override
    public void visit(YanTree.Continue that) {
        if (loopStack.empty()) {
            logger.log(Errors.ContinueNotInLoop(that));
            return;
        }
        that.attachedLoop = loopStack.peek();
    }

    @Override
    public void visit(YanTree.Break that) {
        if (loopStack.empty()) {
            logger.log(Errors.BreakNotInLoop(that));
            return;
        }
        that.attachedLoop = loopStack.peek();
    }
}
