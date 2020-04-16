package yan.foundation.compiler.frontend.lex;


public class Token {

    static public final int EOF = -1;

    /**
     * Token的类型
     */
    final public int type;

    /**
     * Token第一个字符所在行, 从1开始
     */
    final public int line;

    /**
     * Token第一个字符所在列, 从1开始
     */
    final public int col;

    /**
     * Token位于文本的起始位置
     */
    final public int start;

    /**
     * Token位于文本的终止位置
     */
    final public int stop;

    /**
     * 该Token在Token序列中的下标
     */
    final public int index;

    /**
     * Token所在的源代码, 一般用于错误提示
     */
    final public CodeSource source;

    /**
     * 创建Token的Lexer, 一般用于获取{@link Token#type}的字符串表示.
     *
     * @see Lexer#getVocabulary()
     */
    final public Lexer lexer;

    /**
     * Token存储的语义值, 不推荐直接访问, 应先判断Token类型,再通过对应函数访问。
     * 以下为预定义的Token类型和value类型的对应关系:
     * <pre>{@code
     * 1. identifier -> String
     * 2. string_literal -> String
     * 3. int_const -> int
     * 4. bool_const -> boolean
     * 5. 其他 -> 没有值
     * }</pre>
     * Note: 如果你有需求,继承该类创建函数访问你所需的数据类型。
     */
    final public Object value;

    /**
     * Backend field for {@link Token#getText()}
     */
    protected String text;

    public Token(int type, int line, int col, int start, int stop, int index, Object value, CodeSource source, Lexer lexer) {
        this.type = type;
        this.line = line;
        this.col = col;
        this.start = start;
        this.stop = stop;
        this.index = index;
        this.value = value;
        this.source = source;
        this.lexer = lexer;
    }

    public Token(Builder builder) {
        this.type = builder.type;
        this.line = builder.line;
        this.col = builder.col;
        this.start = builder.start;
        this.stop = builder.stop;
        this.index = builder.index;
        this.value = builder.value;
        this.source = builder.source;
        this.lexer = builder.lexer;
    }

    public String getText() {
        if (text == null) {
            text = source.getText(start, stop);
        }
        return text;
    }

    public boolean getBoolValue() {
        return (boolean) value;
    }

    public String getStringValue() {
        return (String) value;
    }

    public int getIntValue() {
        return (int) value;
    }

    public String getTypeString() {
        if (lexer.getVocabulary() != null)
            return lexer.getVocabulary().get(type);
        return String.valueOf(type);
    }

    @Override
    public String toString() {
        return "LexerToken{" +
                "type=" + getTypeString() +
                ", line=" + line +
                ", col=" + col +
                ", start=" + start +
                ", stop=" + stop +
                ", source=" + source.getSourceName() +
                ", value=" + value +
                '}';
    }

    public static class Builder {
        protected int type;
        protected int line, col;
        protected int start, stop;
        protected int index;
        protected Object value;
        protected CodeSource source;
        protected Lexer lexer;

        public Builder(int type, int index) {
            this.type = type;
            this.index = index;
        }

        public Builder pos(int line, int col, int start, int stop) {
            this.line = line;
            this.col = col;
            this.start = start;
            this.stop = stop;
            return this;
        }

        public Builder value(Object value) {
            this.value = value;
            return this;
        }

        public Builder source(CodeSource source) {
            this.source = source;
            return this;
        }

        public Builder lexer(Lexer lexer) {
            this.lexer = lexer;
            return this;
        }

        public Token build() {
            return new Token(this);
        }
    }
}
