package yan.foundation.driver;

import picocli.CommandLine;

import java.io.File;
import java.util.Iterator;
import java.util.List;

@CommandLine.Command(mixinStandardHelpOptions = true, usageHelpAutoWidth = true,
        defaultValueProvider = Config.DefaultProvider.class, versionProvider = Config.DefaultProvider.class)
public class Config {
    @CommandLine.Parameters(index = "0", description = "The file to be processed.")
    File inputFile;

    @CommandLine.Option(names = {"-o", "--output"}, defaultValue = "a.out", description = "Output file for result")
    File outputFile;

    @CommandLine.Option(names = {"-t", "--target"},
            completionCandidates = CompilerTargetCandidates.class,
            description = "The stage you want to compile to." +
                    " Valid values: ${COMPLETION-CANDIDATES}. Default: ${DEFAULT-VALUE}")
    String targetName;

    public interface ConfigSpecProvider {
        List<String> getCompilerTargets();

        String getDefaultCompilerTarget();

        String getVersion();
    }

    public static class CompilerTargetCandidates implements Iterable<String> {
        public static ConfigSpecProvider provider;

        @Override
        public Iterator<String> iterator() {
            return provider.getCompilerTargets().iterator();
        }
    }

    public static class DefaultProvider implements CommandLine.IDefaultValueProvider, CommandLine.IVersionProvider {
        public static ConfigSpecProvider provider;

        @Override
        public String defaultValue(CommandLine.Model.ArgSpec argSpec) {
            if (argSpec.paramLabel().equals("<targetName>")) {
                return provider.getDefaultCompilerTarget();
            }
            return null;
        }

        @Override
        public String[] getVersion() {
            return new String[]{provider.getVersion()};
        }
    }
}