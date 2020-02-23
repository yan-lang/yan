package yan.examples.yan;

import picocli.CommandLine.*;
import yan.skeleton.driver.Config;

@Command(description = "Yan C compiler && interpreter.",
        name = "yancc", mixinStandardHelpOptions = true, version = "yancc 3.0")
public class YanConfig extends Config {
    @Option(names = {"-a", "--algorithm"}, description = "MD5, SHA-1, SHA-256, ...")
    public String algorithm = "SHA-1";

}
