package yan.skeleton.driver;

import picocli.CommandLine.*;

import java.io.*;
import java.nio.file.Path;
import java.util.concurrent.Callable;

@Command(description = "Yan compiler && interpreter.",
        name = "yan", mixinStandardHelpOptions = true, version = "yan 1.0")
public class Config implements Callable<Integer> {
    enum CompilingTarget {
        lex, parse, semantic, ir, llvm_ir, binary
    }

    @Parameters(index = "0", description = "The file to be processed.")
    private File inputFile;

    @Option(names = {"-o", "--output"}, defaultValue = "a.out", description = "Output file for result")
    private File outputFile;

    @Option(names = {"-t", "--target"}, defaultValue = "binary", description = "The stage you want to compile to." +
            " Valid values: ${COMPLETION-CANDIDATES}. Default: ${DEFAULT-VALUE}")
    public CompilingTarget target;

    public Path inputPath;

    public FileInputStream inputStream;

    public String input;

    public OutputStream outputStream;

    @Override
    public Integer call() throws IOException {
        /* 做必要的检查 */
        inputStream = new FileInputStream(inputFile);
        inputPath = inputFile.toPath();
//        outputStream = new FileOutputStream(outputFile);
        input = new String(inputStream.readAllBytes());
        return 0;
    }
}
