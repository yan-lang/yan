package yan.skeleton.driver;

import picocli.CommandLine.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


@Command(mixinStandardHelpOptions = true, defaultValueProvider = BaseConfig.DefaultProvider.class)
public class BaseConfig {

    // 命令行参数

    @Parameters(index = "0", description = "The file to be processed.")
    public File inputFile;

    @Option(names = {"-o", "--output"}, defaultValue = "a.out", description = "Output file for result")
    public File outputFile;

    @Option(names = {"-t", "--target"},
            completionCandidates = CompilerTargetCandidates.class,
            description = "The stage you want to compile to." +
                    " Valid values: ${COMPLETION-CANDIDATES}. Default: ${DEFAULT-VALUE}")
    public String target;

    // 根据命令行参数导出的参数, 或可以在运行时进行配置的参数

    /**
     * 从指定源文件中读取出来的源代码。
     */
    public String source;

    /**
     * 各个编译阶段输出结果的流。
     * <p>有三种可能: (1)stdout, (2)解释器的writer, (3)文件。</p>
     * <p>编译模式下，输出到文件。可能考虑支持通过命令行参数配置输出到stdout。解释模式下输出到解释器的writer。</p>
     */
    public PrintWriter out;

    /**
     * 各个阶段输出异常的流。
     */
    public PrintWriter err;

    public List<String> validate() {
        List<String> message = new ArrayList<>();
        try {
            var inputStream = new FileInputStream(inputFile);
            source = new String(inputStream.readAllBytes());
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
