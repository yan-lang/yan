package yan.foundation.driver.error;

/**
 * {@code BaseError}是{@link Error}, {@link Warning}, {@link Note}的基类，主要定义了格式化输出必要的接口。
 * <p>该类实现了一个默认的格式化输出方法{@link Unexpected#toString}。</p>
 * <p>你一般不需要使用到这个类，如果要定义新的error，你可以继承该类的子类Error，Warning等。</p>
 */
public abstract class Unexpected extends RuntimeException {
    protected int line;
    protected int column;
    protected String sourceName;
    protected String message;
    protected String context;
    protected String hint;

    public Unexpected(int line, int column, String sourceName, String message, String context, String hint) {
        this.line = line;
        this.column = column;
        this.sourceName = sourceName;
        this.message = message;
        this.context = context;
        this.hint = hint;
    }

    /**
     * 获取类型的字符串表示
     **/
    abstract public String getType();

    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }

    /**
     * 获取出错代码所在行的文本。
     * <p>这个方法在{@link Unexpected#toString()}中被用来获取上下文信息。</p>
     */
    public String getContext() {
        return context;
    }

    /**
     * 获取源代码的来源的名称。
     * <p>通常来说，编译某个文件出错时这个函数返回的是文件名。</p>
     * <p>解释某段代码返回的是{@code <stdin>}</p>
     */
    public String getSourceName() {
        return sourceName;
    }

    public String getMessage() {
        return message;
    }

    public String getHint() {
        return hint;
    }

    /**
     * 默认格式化输出错误为字符串。格式如下:
     * <pre>
     * 代码源名称:行号:列号 错误类型: 错误信息
     * 错误代码上下文
     * 指示箭头
     * 提示缺少字符[可选]
     * ...
     * 错误数目提示
     * </pre>
     *
     * <p>
     * 例子:
     * <pre>
     * test.c:2:9: yan.common.error: expected ';' after expression
     *         print()
     *                ^
     *                ;
     * </pre>
     * </p>
     *
     * @return 格式化的错误表示
     */
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(getSourceName()).append(":").append(getLine()).append(":").append(getColumn()).append(": ");
        builder.append(getType()).append(": ").append(getMessage()).append('\n');

        // 构造上下文:
        // TODO: 如果出错位置前面或后面字符过多（超过80个字符）, 保留前面25个字符，后面50个字符其他用省略号替代

        if (context == null) return builder.toString();
        builder.append(context).append('\n');
        builder.append(padPrefixSpace(getColumn() - 1, "^"));
        if (getHint() != null) builder.append('\n').append(padPrefixSpace(getColumn() - 1, getHint()));

        return builder.toString();
    }

    private String padPrefixSpace(int num, String text) {
        return " ".repeat(num).concat(text);
    }
}