package yan.skeleton.compiler.frontend.lex;


import javax.swing.text.Position;

public class LexerToken {

    /* Token的类型 */
    final public int type;

    /* Token第一个字符所在行, 从1开始 */
    final public int line;

    /* Token第一个字符所在列, 从1开始 */
    final public int col;

    /* Token位于文本的起始位置 */
    final public int start;

    /* Token位于文本的终止位置 */
    final public int stop;

    /* Token所在的源代码, 一般用于错误提示 */
    final public String source;

    /**
     * Token存储的语义值, 不可以直接访问, 应先判断Token类型,再通过对应函数访问。
     * 以下为预定义的Token类型和value类型的对应关系:
     * 1. identifier -> String
     * 2. string_literal -> String
     * 3. int_const -> int
     * 4. bool_const -> boolean
     * 5. 其他 -> 没有值
     * Note: 如果你有需求,继承该类创建函数访问你所需的数据类型。
     */
    final public Object value;

    public LexerToken(int type, Object value, Position pos, String source) {
        this.type = type;
        this.value = value;
        this.pos = pos;
        this.source = source;
    }

    public boolean getBoolValue() {
        return (boolean) value;
    }

    public String getStrValue() {
        return (String) value;
    }

    public int getIntValue() {
        return (int) value;
    }

    @Override
    public String toString() {
        return "LexerToken{" +
                "type=" + type +
                ", value=" + value +
                ", pos=" + pos +
                '}';
    }
}
