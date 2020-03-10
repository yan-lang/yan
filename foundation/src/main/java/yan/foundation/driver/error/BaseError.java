package yan.foundation.driver.error;

import yan.foundation.compiler.frontend.lex.CodeSource;

/**
 * {@code BaseError}是{@link Error}, {@link Warning}, {@link Note}的基类，主要定义了格式化输出必要的接口。
 * <p>该类实现了一个默认的格式化输出方法{@link BaseError#toString}。</p>
 * <p>你一般不需要使用到这个类，如果要定义新的error，你可以继承该类的子类Error，Warning等。</p>
 */
public abstract class BaseError {
    protected CodeSource source;
    protected int line;
    protected int column;
    protected String message;
    protected String hint;

    public BaseError(CodeSource source, int line, int column, String message, String hint) {
        this.source = source;
        this.line = line;
        this.column = column;
        this.message = message;
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
     * 获取代码来源。
     * <p>这个方法在{@link BaseError#toString()}中被用来获取上下文信息。</p>
     */
    public CodeSource getSource() {
        return source;
    }

    /**
     * 获取源代码的来源的名称。
     * <p>通常来说，编译某个文件出错时这个函数返回的是文件名。</p>
     * <p>解释某段代码返回的是{@code <stdin>}</p>
     * <p>函数的默认实现使用{@code Code}的{@link CodeSource#getSourceName()}方法。</p>
     */
    public String getSourceName() {
        return getSource().getSourceName();
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
     * test.c:2:9: error: expected ';' after expression
     *         print()
     *                ^
     *                ;
     * </pre>
     * </p>
     * @return 格式化的错误表示
     */
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(getSourceName()).append(":").append(getLine()).append(":").append(getColumn()).append(": ");
        builder.append(getType()).append(": ").append(getMessage()).append('\n');

        // 构造上下文:
        // TODO: 如果出错位置前面或后面字符过多（超过80个字符）, 保留前面25个字符，后面50个字符其他用省略号替代

        String allContext = getSource().get(getLine());
        String context = allContext;
        builder.append(context).append('\n');
        builder.append(padPrefixSpace(getColumn(), "^")).append('\n');
        builder.append(padPrefixSpace(getColumn(), getHint()));

        return builder.toString();
    }

    private String padPrefixSpace(int num, String text) {
        return " ".repeat(num).concat(text);
    }
}
