package yan.lang.predefine;

import yan.lang.predefine.YanTree.*;

//@formatter:off
public interface YanVisitor<R> {
    default R visitOthers(YanTree that) { return null; }
    default R visit(Program that)       { return visitOthers(that);}
    default R visit(VarDef that)        { return visitOthers(that); }
    default R visit(ClassDef that)      { return visitOthers(that); }
    default R visit(FuncDef that)       { return visitOthers(that); }
    default R visit(Return that)        { return visitOthers(that); }
    default R visit(If that)            { return visitOthers(that); }
    default R visit(While that)         { return visitOthers(that); }
    default R visit(Continue that)      { return visitOthers(that); }
    default R visit(Break that)         { return visitOthers(that); }
    default R visit(ExprStmt that)      { return visitOthers(that); }
    default R visit(Block that)         { return visitOthers(that); }
    default R visit(Empty that)         { return visitOthers(that); }
    default R visit(Operator that)      { return visitOthers(that); }
    default R visit(Unary that)         { return visitOthers(that); }
    default R visit(Binary that)        { return visitOthers(that); }
    default R visit(TypeCast that)      { return visitOthers(that); }
    default R visit(FunCall that)       { return visitOthers(that); }
    default R visit(Identifier that)    { return visitOthers(that); }
    default R visit(Literal that)       { return visitOthers(that); }
}
