package yan.foundation.driver;


import picocli.CommandLine;
import picocli.CommandLine.ParameterException;
import picocli.CommandLine.UnmatchedArgumentException;
import yan.foundation.driver.lang.Language;
import yan.foundation.driver.shell.Repl;

import java.util.ArrayList;
import java.util.List;

public class Launcher implements Config.ConfigSpecProvider {

    // -------- Static Methods -------- //

    public static void launch(Language language, String[] args) {
        int exitCode = new Launcher(language).launch(args);
        System.exit(exitCode);
    }

    public static Launcher instance(Language language) {
        return new Launcher(language);
    }

    // -------- Instance Methods -------- //

    Language language;
    String commandName = "<main>";

    private Launcher(Language language) { this.language = language; }

    public Launcher commandName(String commandName) {
        this.commandName = commandName;
        return this;
    }

    public int launch(String[] args) {
        if (args.length == 0) {
            return runInterpreter();
        } else {
            return runCompiler(args);
        }
    }

    private int runInterpreter() {
        return Repl.run(new ScriptEngineImpl(language));
    }

    private int runCompiler(String[] args) {
        // parse args
        Config config = new Config();
        Config.CompilerTargetCandidates.provider = this;
        Config.DefaultProvider.provider = this;
        final CommandLine cmd = new CommandLine(config);
        cmd.setCommandName(commandName);
        try {
            CommandLine.ParseResult parseResult = cmd.parseArgs(args);
            if (parseResult.isUsageHelpRequested()) {
                cmd.usage(cmd.getOut());
                return cmd.getCommandSpec().exitCodeOnUsageHelp();
            } else if (parseResult.isVersionHelpRequested()) {
                cmd.printVersionHelp(cmd.getOut());
                return cmd.getCommandSpec().exitCodeOnVersionHelp();
            }
        } catch (ParameterException ex) {
            cmd.getErr().println(ex.getMessage());
            if (!UnmatchedArgumentException.printSuggestions(ex, cmd.getErr())) {
                ex.getCommandLine().usage(cmd.getErr());
            }
            return cmd.getCommandSpec().exitCodeOnInvalidInput();
        }
        // TODO(2020-4-21): validate target name
        if (!getCompilerTargets().contains(config.targetName)) {
            cmd.getOut().println("invalid target name: " + config.targetName);
            cmd.getOut().println("allowed values: " + getCompilerTargets());
            return cmd.getCommandSpec().exitCodeOnInvalidInput();
        }

        // run with parsed args
        return language.compile(config.inputFile, config.outputFile, config.targetName);
    }

    // -------- Config -------- //

    @Override
    public List<String> getCompilerTargets() {
        List<String> targets = new ArrayList<>();
        language.getTargets().forEach(target -> targets.add(target.name));
        return targets;
    }

    @Override
    public String getDefaultCompilerTarget() {
        var targets = getCompilerTargets();
        if (!targets.isEmpty()) return targets.get(targets.size() - 1);
        else return null;
    }

    @Override
    public String getVersion() {
        return language.version();
    }

}
