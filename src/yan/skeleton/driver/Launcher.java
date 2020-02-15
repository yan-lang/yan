package yan.skeleton.driver;

import picocli.CommandLine;
import yan.skeleton.interpreter.Repl;

import java.lang.reflect.InvocationTargetException;

import picocli.CommandLine.*;


public class Launcher {

    Class<? extends Config> configType = Config.class;

    public void setConfigType(Class<? extends Config> configType) {
        this.configType = configType;
    }

    public void launch(String[] args) throws NoSuchMethodException, IllegalAccessException,
            InvocationTargetException, InstantiationException {
        if (args.length == 0) {
            // 启动解释器
            Repl.run();
        } else {
            Config config = configType.getDeclaredConstructor().newInstance();
            int exitCode = new CommandLine(config)
                    .setExecutionExceptionHandler(new PrintExceptionMessageHandler())
                    .execute(args);
            if(exitCode != 0) System.exit(exitCode);
            else launchCompiler(config);
        }
    }

    private void launchCompiler(Config config) {

    }
}

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
