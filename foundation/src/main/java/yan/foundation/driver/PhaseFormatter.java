package yan.foundation.driver;

// TODO: 可以继续重构, PhaseFormatter -> Serializer
public interface PhaseFormatter<Out> {

    // TODO: 输出不一定是字符串，也许是二进制数据，需要改进。
    String toString(Out out);

    /**
     * 输出文件的后缀名
     *
     * @return 如果有则输出后缀名, 没有的话返回空字符串或者null.
     */
    String fileExtension();

    /**
     * Target name 会被用于编译器的命令行界面中, 作为指定编译到某个阶段的名称。
     * <pre>
     * 例: 假如 targetName=lex，则命令行的target选项的可选值中间就会有lex。
     *     java -jar example.jar --target lex
     *     上面这个命令就会编译到lex这个阶段。
     * </pre>
     *
     * @return target name
     */
    String targetName();

}
