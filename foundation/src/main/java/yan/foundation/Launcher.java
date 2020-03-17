package yan.foundation;

import picocli.CommandLine;
import picocli.CommandLine.ParameterException;
import picocli.CommandLine.ParseResult;
import picocli.CommandLine.UnmatchedArgumentException;
import yan.foundation.interpreter.Interpretable;
import yan.foundation.interpreter.Repl;

import java.util.List;


public class Launcher {

    public static void launch(Language language, String[] args) {
        int exitCode = new Launcher(language).launch(args);
        System.exit(exitCode);
    }

    public final Language language;

    public Launcher(Language language) {
        this.language = language;
    }

    public int launch(String[] args) {
        // 没有任何参数的情况下启动解释器(如果语言可解释的话)
        if (args.length == 0 && language instanceof Interpretable) {
            return Repl.run((Interpretable) language);
        }
        return runCompiler(args);
    }

    private int runCompiler(String[] args) {
        // 解析命令行参数, 正常则启动编译器, 否则退出
        final CommandLine cmd = new CommandLine(language.config);
        try {
            ParseResult parseResult = cmd.parseArgs(args);
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

        // 验证参数是否合法等
        List<String> message = language.config.validate();
        if (!message.isEmpty()) {
            message.forEach(System.out::println);
            return cmd.getCommandSpec().exitCodeOnInvalidInput();
        }
        System.out.println("Cong! You are in.");
        return language.compile();
    }
}