package yan.skeleton.driver;

import picocli.CommandLine;
import picocli.CommandLine.*;

import java.io.*;
import java.nio.file.Path;
import java.util.concurrent.Callable;

@Command(mixinStandardHelpOptions = true)
public class BaseConfig implements Callable<Integer> {


    @Parameters(index = "0", description = "The file to be processed.")
    private File inputFile;

    @Option(names = {"-o", "--output"}, defaultValue = "a.out", description = "Output file for result")
    private File outputFile;

    public Path inputPath;

    public String input;

    @Override
    public Integer call() throws IOException {
        /* 做必要的检查 */
        var inputStream = new FileInputStream(inputFile);
        inputPath = inputFile.toPath();
//        outputStream = new FileOutputStream(outputFile);
        input = new String(inputStream.readAllBytes());
        return 0;
    }
}
