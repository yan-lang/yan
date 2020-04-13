package yan.lang.predefine;

//@formatter:off
public interface YanVisitor<R> {
    default R visitOthers(YanTree that)      { return null; }
    default R visit(YanTree.Program that)    { return visitOthers(that); }
    default R visit(YanTree.VarDef that)     { return visitOthers(that); }
    default R visit(YanTree.If that)         { return visitOthers(that); }
    default R visit(YanTree.ExprStmt that)   { return visitOthers(that); }
    default R visit(YanTree.Block that)      { return visitOthers(that); }
    default R visit(YanTree.Print that)      { return visitOthers(that); }
    default R visit(YanTree.Empty that)      { return visitOthers(that); }
    default R visit(YanTree.Unary that)      { return visitOthers(that); }
    default R visit(YanTree.Binary that)     { return visitOthers(that); }
    default R visit(YanTree.Identifier that) { return visitOthers(that); }
    default R visit(YanTree.Literal that)   { return visitOthers(that); }
}
