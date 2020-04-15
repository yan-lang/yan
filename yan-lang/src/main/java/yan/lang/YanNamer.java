package yan.lang;

import yan.foundation.compiler.frontend.semantic.*;
import yan.lang.predefine.YanTree;
import yan.lang.predefine.abc.AbstractNameResolver;

/**
 * {@code YanNamer}负责解析程序中的符号，建立符号表，检查引用的符号是否被定义。
 * <pre>
 *     建立符号表:
 *        1. 变量定义
 *        2. 函数定义
 *        3. 类定义
 *     符号引用:
 *        1. 表达式中的变量引用
 *        2. 函数调用
 *        3. 类型名称
 *        4. 父类名称
 *        5. 对象名.方法 / 对象名.变量 (判断对象所在类是否有该方法和变量)
 *        6. 新建对象(类名)
 * </pre>
 */
public class YanNamer extends AbstractNameResolver {

    Scope currentScope;

    @Override
    public void visit(YanTree.Program program) {
        currentScope = ScopeFactory.GlobalScope();
        program.defs.forEach(def -> def.accept(this));
    }

    @Override
    public void visit(YanTree.VarDef varDef) {
        currentScope.define(new VarSymbol(varDef.id.name, varDef));
        out.println("define: " + varDef.id.name);
    }

    @Override
    public void visit(YanTree.ClassDef that) {
        // do nothing, we don't process class for now.
    }

    @Override
    public void visit(YanTree.FuncDef that) {
        Symbol sym = currentScope.find(that.id.name);
        if (sym instanceof MethodSymbol) { // instanceof contain null check
            logger.log(Errors.FunctionAlreadyDefined());
        } else {
            MethodSymbol symbol = new MethodSymbol(that.id.name, null, that, currentScope);
            that.params.forEach(param -> symbol.define(new VarSymbol(param.id.name, param)));
            out.println(symbol);
            currentScope.define(symbol);
            currentScope = symbol;
            that.body.accept(this);
        }
    }

    @Override
    public void visit(YanTree.Block block) {
        currentScope = ScopeFactory.LocalScope(currentScope);
        block.stmts.forEach(stmt -> stmt.accept(this));
        currentScope = currentScope.getEnclosingScope();
    }

    @Override
    public void visit(YanTree.Identifier identifier) {
        identifier.symbol = currentScope.resolve(identifier.name);
        if (identifier.symbol == null) {
            logger.log(Errors.VariableNotDefine());
        }
    }
}
