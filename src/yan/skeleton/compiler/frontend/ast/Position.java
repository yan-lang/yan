package yan.skeleton.compiler.frontend.ast;

/**
 * 记录Token的(起始/终止)位置.
 */
public class Position {

    /* Token所在行, 从1开始 */
    final public int line;

    /* Token所在列, 从1开始 */
    final public int col;

    public Position(int line, int col) {
        this.line = line;
        this.col = col;
    }

    @Override
    public String toString() {
        return "Position{" +
                "line=" + line +
                ", col=" + col +
                '}';
    }
}
