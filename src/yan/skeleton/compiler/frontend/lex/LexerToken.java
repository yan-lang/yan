package yan.skeleton.compiler.frontend.lex;

import yan.skeleton.compiler.frontend.ast.Position;

public class LexerToken {

    final public int type;

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
    final protected Object value;

    /* Token所在起始位置 */
    final public Position pos;


    public LexerToken(int type, Object value, Position pos) {
        this.type = type;
        this.value = value;
        this.pos = pos;
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
