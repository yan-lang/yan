package yan.skeleton.compiler.frontend.lex;

/**
 * {@code CodeSource}主要用于记录{@link LexerToken}所在的代码源。
 * 在报错系统中，这个接口定义了格式化输出报错信息所必要的函数。
 */
public interface CodeSource{
    /**
     * 获取代码源的名称
     *
     * @return 代码源名称
     */
    String getSourceName();

    /**
     * 获取代码源的全部代码
     *
     * @return 全部代码
     */
    String get();

    /**
     * 获取代码源中某一行的代码
     *
     * @param line 行号
     * @return 第line行的代码
     */
    String get(int line);

    /**
     * 返回一个字符串。该字符串是源代码中{@code begin}到{@code end}的子字符串。
     *
     * @param begin 起始下标
     * @param end   终止下标
     * @return 子字符串
     */
    default String getText(int begin, int end) {
        return get().substring(begin, end);
    }

    /**
     * 返回代码长度。
     *
     * @return 代码长度
     */
    default int length() {
        return get().length();
    }


}
