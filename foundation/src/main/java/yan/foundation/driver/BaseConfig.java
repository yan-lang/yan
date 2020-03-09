package yan.foundation.driver;

import picocli.CommandLine.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


@Command(mixinStandardHelpOptions = true, defaultValueProvider = BaseConfig.DefaultProvider.class)
public class BaseConfig {

    // ============= 命令行参数 ============ //

    @Parameters(index = "0", description = "The file to be processed.")
    public File inputFile;

    @Option(names = {"-o", "--output"}, defaultValue = "a.out", description = "Output file for result")
    public File outputFile;

    /**
     * 编译目标。这个参数指示编译器将要编译源程序到哪个阶段。
     * <p>可选值和默认值由{@link Language#getCompilerTargets()}，{@link Language#getDefaultCompilerTarget()}决定。
     * </p>
     */
    @Option(names = {"-t", "--target"},
            completionCandidates = CompilerTargetCandidates.class,
            description = "The stage you want to compile to." +
                    " Valid values: ${COMPLETION-CANDIDATES}. Default: ${DEFAULT-VALUE}")
    public String target;

    // =========== 根据命令行参数导出的参数, 或可以在运行时进行配置的参数 =========== //

    /**
     * 从指定源文件中读取出来的源代码。
     */
    public String source;

    /**
     * 各个编译阶段输出结果的流。
     * <p>有三种可能: (1)stdout, (2)解释器的writer, (3)文件。</p>
     * <p>编译模式下，输出到文件。可能考虑支持通过命令行参数配置输出到stdout。
     * 解释模式下输出到解释器的writer。</p>
     */
    public PrintWriter out;

    /**
     * 各个阶段输出异常的流。默认是{@code System.err}。
     */
    public PrintStream err = System.err;

    /**
     * 获取输入源的名称
     *
     * @return 如果输入是文件, 则返回文件名; 如果输入是控制台, 则返回stdin.
     */
    public String getInputSourceName() {
        if (inputFile != null) return inputFile.getName();
        return "stdin";
    }

    /**
     * 再次验证参数是否正确，并根据参数计算一些导出值。
     * <p>该函数将会在{@link Launcher#launch(String[])}中被调用。
     *    如果你需要在你的子类中验证一些你自己定义的参数，或计算导出值，请重写该函数，
     *    并在重写的函数中调用父类的{@code validate}方法。
     * </p>
     * 例:
     * <pre>
     * {@code @override
     * public List<String> validate() {
     *     super.validate()
     *     // 编写你的逻辑
     * }
     * }</pre>
     * @return 错误信息列表。如果没有错的话，请返回一个空列表。
     */
    public List<String> validate() {
        List<String> message = new ArrayList<>();
        try {
            var inputStream = new FileInputStream(inputFile);
            source = new String(inputStream.readAllBytes());
            out = new PrintWriter(outputFile);
        } catch (IOException e) {
            message.add(e.getMessage());
        }
        return message;
    }

    static class CompilerTargetCandidates implements Iterable<String> {
        static Language<?> language;

        @Override
        public Iterator<String> iterator() {
            return language.getCompilerTargets().iterator();
        }
    }

    static class DefaultProvider implements IDefaultValueProvider {
        static Language<?> language;

        @Override
        public String defaultValue(Model.ArgSpec argSpec) throws Exception {
            if (argSpec.paramLabel().equals("<target>")) {
                return language.getDefaultCompilerTarget();
            }
            return null;
        }
    }
}
