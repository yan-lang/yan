package yan.skeleton.compiler.frontend.lex;


public class LexerToken {

    static public final int EOF = -1;

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
    final public CodeSource source;

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


    public LexerToken(int type, int line, int col, int start, int stop, Object value, CodeSource source) {
        this.type = type;
        this.line = line;
        this.col = col;
        this.start = start;
        this.stop = stop;
        this.value = value;
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
        return toString(null);
    }

    public String toString(Lexer lexer) {
        String typeString = lexer.getVocabulary().get(type);
        return "LexerToken{" +
                "type=" + typeString +
                ", line=" + line +
                ", col=" + col +
                ", start=" + start +
                ", stop=" + stop +
                ", source=" + source.getSourceName() +
                ", value=" + value +
                '}';
    }
}
