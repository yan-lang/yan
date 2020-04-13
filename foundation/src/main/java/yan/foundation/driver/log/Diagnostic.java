package yan.foundation.driver.log;


public class Diagnostic extends RuntimeException {
    public static final String ERROR = "error";
    public static final String WARNING = "warning";
    public static final String NOTE = "note";

    public String type;

    public int line;
    public int column;
    public int length;
    public String sourceName;

    public String message;
    public String context;
    public String hint;

    public Diagnostic(String message) {
        this.message = message;
        this.type = ERROR;
    }

    public boolean is(String type) {
        return this.type.equals(type);
    }

    /**
     * 默认格式化输出诊断信息为字符串。格式如下:
     * <pre>
     * 代码源名称:行号:列号 错误类型: 错误信息
     * 错误代码上下文
     * 指示箭头(^~~~~~)
     * 提示缺少字符[可选]
     * ...
     * 错误数目提示
     * </pre>
     * <p>
     * 例子:
     * <pre>
     * test.c:2:9: error: expected ';' after expression
     *         print()
     *                ^
     *                ;
     * </pre>
     *
     * @return 格式化的错误表示
     */
    @Override
    public String getMessage() {
        StringBuilder builder = new StringBuilder();
        builder.append(sourceName).append(":").append(line).append(":").append(column).append(": ");
        builder.append(type).append(": ").append(message).append('\n');

        // 构造上下文:
        // TODO: 如果出错位置前面或后面字符过多（超过80个字符）, 保留前面25个字符，后面50个字符其他用省略号替代

        if (context == null) return builder.toString();
        builder.append(context).append('\n');
        builder.append(padPrefixSpace(column - 1, "^"));
        if (hint != null) builder.append('\n').append(padPrefixSpace(column - 1, hint));

        return builder.toString();
    }

    private String padPrefixSpace(int num, String text) {
        return " ".repeat(num).concat(text);
    }
}
