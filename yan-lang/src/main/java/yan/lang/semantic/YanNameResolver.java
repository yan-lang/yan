package yan.lang.semantic;

import yan.foundation.compiler.frontend.semantic.v1.Scope;
import yan.foundation.compiler.frontend.semantic.v1.Symbol;
import yan.foundation.compiler.frontend.semantic.v1.Type;
import yan.foundation.compiler.frontend.semantic.v1.symbol.MethodSymbol;
import yan.foundation.compiler.frontend.semantic.v1.symbol.TypeSymbol;
import yan.foundation.compiler.frontend.semantic.v1.symbol.VarSymbol;
import yan.foundation.compiler.frontend.semantic.v1.type.MethodType;
import yan.foundation.driver.log.Diagnostic;
import yan.lang.predefine.YanTree;
import yan.lang.predefine.YanTypes;
import yan.lang.predefine.abc.AbstractNameResolver;

public class YanNameResolver extends AbstractNameResolver {

    Scope currentScope;

    @Override
    public void visit(YanTree.Program program) {
        currentScope = program.scope;
        program.defs.forEach(def -> def.accept(this));
    }

    @Override
    public void visit(YanTree.VarDef varDef) {
        var previous = currentScope.resolveLocally(varDef.id.name);
        if (previous != null) {
            logger.log(Errors.SymbolAlreadyDefined()); // duplicate definition
        }

        var varSymbol = new VarSymbol(varDef.id.name);
        varSymbol.tree = varDef;

        if (varDef.varType != null) {
            // All type definitions should be resolve by the previous phase TypeBuilder
            varSymbol.type = resolveType(varDef.varType.name);
        }

        currentScope.define(varSymbol);
        varDef.symbol = varSymbol;
    }

    public Type resolveType(String name) {
        Symbol symbol = currentScope.resolve(name);
        if (symbol instanceof TypeSymbol) {
            return ((TypeSymbol) symbol).getType();
        } else {
            logger.log(Errors.InvalidSymbol()); // not a type, we could tell what it is via symbol.kind
            return YanTypes.Error;
        }
    }

    @Override
    public void visit(YanTree.ClassDef that) {
        // ignore for now
    }

    @Override
    public void visit(YanTree.FuncDef that) {
        var previous = currentScope.resolveLocally(that.id.name);
        if (previous != null) {
            logger.log(Errors.SymbolAlreadyDefined()); // duplicate definition
            return;
        }

        // define symbol
        that.symbol = new MethodSymbol(that.id.name, new MethodType());

        // add to current scope
        currentScope.define(that.symbol);

        // process return type
        that.symbol.methodType.retType = that.retType == null ? YanTypes.Void : resolveType(that.retType.name);

        // process parameter type
        currentScope = new Scope(currentScope);
        that.params.forEach(param -> {
            // formal parameter must have a type
            param.accept(this);
            that.symbol.methodType.argTypes.add(param.symbol.type);
        });

        // process body
        that.body.accept(this);

        currentScope = currentScope.getEnclosingScope();
    }

    @Override
    public void visit(YanTree.Block block) {
        currentScope = new Scope(currentScope);
        block.stmts.forEach(stmt -> stmt.accept(this));
        currentScope = currentScope.getEnclosingScope();
    }

    @Override
    public void visit(YanTree.TypeCast that) {
        that.evalType = resolveType(that.castedType.name);
        that.expr.accept(this);
    }

    @Override
    public void visit(YanTree.FunCall that) {
        that.funcName.symbol = currentScope.resolve(that.funcName.name);
        if (!(that.funcName.symbol instanceof MethodSymbol)) {
            logger.log(Errors.MethodNotDefine());
        } else {
            // check arity
            var expected = ((MethodSymbol) that.funcName.symbol).methodType.argTypes.size();
            var actual = that.args.size();
            if (expected != actual) {
                logger.log(Diagnostic.Error("arity mismatched, expect " +
                                            expected + " args, but got " + actual + " args."));
            }
        }
    }

    @Override
    public void visit(YanTree.Identifier identifier) {
        // WARNING: this method should only handle identifiers in expression.
        identifier.symbol = currentScope.resolve(identifier.name);
        if (!(identifier.symbol instanceof VarSymbol)) {
            logger.log(Errors.VariableNotDefine());
        }
    }
}
