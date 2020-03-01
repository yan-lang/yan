package yan.skeleton.driver;

import picocli.CommandLine;
import yan.examples.yan.semantic.YanNameResolver;
import yan.examples.yan.tree.YanTree;
import yan.skeleton.interpreter.Repl;

import picocli.CommandLine.*;
import yan.skeleton.interpreter.ScriptEngine;

import java.util.ArrayList;


public class Launcher {

    Language language;

    public Launcher(Language language) {
        this.language = language;
    }

    public void launch(String[] args) {
        if (args.length == 0 && language instanceof ScriptEngine) { // 启动解释器
            Repl.run((ScriptEngine) language);
        } else {
            // 解析命令行参数, 正常则启动编译器, 否则退出
            int exitCode = new CommandLine(language.getConfig())
                    .setExecutionExceptionHandler(new PrintExceptionMessageHandler())
                    .execute(args);
            if (exitCode != 0) System.exit(exitCode);
            else launchCompiler();
        }
    }

    private void launchCompiler() {
//        YanLexer lexer = new YanLexer("x", language.getConfig());
//        var optOut = lexer.apply(language.getConfig().input);
//        optOut.ifPresent(lexerTokens -> System.out.println(lexer.stringfy(lexerTokens)));
        YanNameResolver nameResolver = new YanNameResolver("Name Resolver", language.getConfig());
        var funcList = new ArrayList<YanTree.Function>();
        funcList.add(new YanTree.Function(new Position(1, 1, 1, stop)));
        nameResolver.apply(new YanTree.Program(new Position(1, 1, 1, stop), funcList));
    }
}

/**
 * 用于处理解析命令行参数时抛出的异常.
 */
class PrintExceptionMessageHandler implements IExecutionExceptionHandler {
    public int handleExecutionException(Exception ex,
                                        CommandLine commandLine,
                                        ParseResult parseResult) {

        commandLine.getErr().println(ex.getMessage());

        return commandLine.getExitCodeExceptionMapper() != null
                ? commandLine.getExitCodeExceptionMapper().getExitCode(ex)
                : commandLine.getCommandSpec().exitCodeOnExecutionException();
    }
}
