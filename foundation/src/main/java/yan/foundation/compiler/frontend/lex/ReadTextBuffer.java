package yan.foundation.compiler.frontend.lex;

import java.util.List;
import java.util.stream.Collectors;

public class ReadTextBuffer implements CodeSource {

    // region Properties

    protected String source;    // 源代码
    protected String sourceName;
    protected List<String> lines;
    protected int length;       // 源代码总长度(总字符数)

    protected int offset;       // current offset (in character)
    protected int line;         // current line
    protected int col;          // current column

    protected int marked_offset;
    protected int marked_line;
    protected int marked_col;

    protected int previousLine;         // previous line
    protected int previousCol;          // previous column

    // endregion

    // region Public Methods

    public ReadTextBuffer(String source, String sourceName) {
        this.source = source;
        this.sourceName = sourceName;
        this.length = source.length();
        reset();
    }

    public int getOffset() { return offset; }

    public int getLine() { return line; }

    public int getCol() { return col; }

    /**
     * 重置缓冲区,offset清0,line和col归1。
     */
    public void reset() {
        offset = 0;
        line = 1;
        col = 1;
    }

    /**
     * 标记当前位置。
     * <p>标记的位置可以通过{@link ReadTextBuffer#marked_offset}，
     * {@link ReadTextBuffer#marked_line}，
     * {@link ReadTextBuffer#marked_col}访问。</p>
     */
    public void mark() {
        marked_offset = offset;
        marked_line = line;
        marked_col = col;
    }

    /**
     * 消耗<strong>当前字符</strong>，返回<strong>下一个字符</strong>。
     *
     * <p>如果当前字符是最后一个字符，则函数将返回一个特殊字符EOF - '\0'，
     * 之后再调用这个函数，不会抛出异常，仍然返回EOF。</p>
     *
     * @return 刚刚消耗的字符
     */
    public char consume() {
        if (offset < length) {
            updateLoc();
            offset += 1;
        }
        return previous();
    }

    /**
     * 返回<strong>当前字符</strong>。当所有字符都消耗完后，该函数返回EOF。
     *
     * @return 当前字符
     */
    public char previous() {
        if (offset == 0) return current();
        return offset - 1 < length ? source.charAt(offset - 1) : '\0';
    }

    /**
     * 返回<strong>当前字符</strong>。当所有字符都消耗完后，该函数返回EOF。
     *
     * @return 当前字符
     */
    public char current() {
        return offset < length ? source.charAt(offset) : '\0';
    }

    /**
     * 判断<strong>当前字符</strong>是否是{@code chs}中的一个字符。
     * <p>若成立，该函数会调用{@link ReadTextBuffer#consume()}</p>消耗掉当前字符。
     *
     * @param chs 待匹配字符(组)
     * @return 是否匹配成功
     */
    public boolean current(char... chs) {
        char cur_ch = current();
        for (char ch : chs) {
            if (cur_ch == ch) {
                consume();
                return true;
            }
        }
        return false;
    }

    /**
     * 预览<strong>下一个字符</strong>。
     * <p>与{@link ReadTextBuffer#consume()}不同的是，这个函数不会消耗当前字符。</p>
     *
     * @return 下一个字符。
     */
    public char peek() {
        return offset + 1 < length ? source.charAt(offset + 1) : '\0';
    }

    /**
     * 判断<strong>下一个字符</strong>是否是{@code chs}中的某个字符。
     * <p>该函数与{@link ReadTextBuffer#current(char...)}类似。</p>
     *
     * @param chs 待匹配字符(组)
     * @return 是否匹配成功
     */
    public boolean peek(char... chs) {
        char cur_ch = peek();
        for (char ch : chs) {
            if (cur_ch == ch) {
                consume();
                return true;
            }
        }
        return false;
    }

    public String substring(int start, int end) {
        return source.substring(start, end);
    }

    // endregion

    // region Private Methods

    /**
     * Update current character's location
     **/
    private void updateLoc() {
        previousLine = line;
        previousCol = col;

        char ch = current();
        if (ch == '\n') {
            line += 1;
            col = 0;
        }
        // TODO(2020-5-9): 隐藏字符需要特殊处理, \t的空格数有可能是2或4
        if (ch == '\t') col += 4;
        else col += 1;
    }

    // endregion

    @Override
    public String getSourceName() {
        return sourceName;
    }

    @Override
    public String get() {
        return source;
    }

    @Override
    public String get(int line) {
        if (lines == null) {
            lines = source.lines().collect(Collectors.toList());
        }
        return lines.get(line - 1);
    }

}
