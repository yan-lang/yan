package yan.foundation.driver.shell;

import org.jline.terminal.Terminal;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

public class ConsoleEngine {

    // region: Exceptions

    public static class DebugException extends Exception {
        public boolean shouldEnable;

        public DebugException(boolean shouldEnable) {
            this.shouldEnable = shouldEnable;
        }
    }

    public static class QuitException extends Exception {
    }

    public static class ClearException extends Exception {
    }

    private final ScriptEngine scriptEngine;
    private final Terminal terminal;
    private final Vector<String> scripts = new Vector<>();

    // endregion

    public ConsoleEngine(ScriptEngine scriptEngine, Terminal terminal) {
        this.scriptEngine = scriptEngine;
        this.terminal = terminal;
    }

    public Object execute(String line) throws Exception {
        if (!line.startsWith(":")) {
            scripts.add(line);
            scriptEngine.execute(line, new PrintStream(terminal.output()), new PrintStream(terminal.output()));
            return null;
        } else {
            String cmd = line.substring(1);
            if (cmd.startsWith("save")) {
                return save2File(cmd);
            }
            if (cmd.startsWith("target")) {
                return switchTarget(cmd);
            }
            switch (cmd) {
                case "help":
                    return help();
                case "quit":
                    throw new QuitException();
                case "debug":
                    throw new DebugException(true);
                case "quit debug":
                    throw new DebugException(false);
                case "clear":
                    throw new ClearException();
                default:
                    return unrecognized();
            }
        }
    }

    private Object switchTarget(String cmd) {
        String[] args = cmd.split(" ");
        if (cmd.equals("target")) return scriptEngine.getTarget();
        if (args.length != 2)
            return "Invalid target switch command: \n" +
                    "Usage:\n" +
                    ":target [targetName]";
        String target = args[1];
        if (!scriptEngine.getTargets().contains(target))
            return target + " is not a valid target name.\n" +
                    "Allowed target names include: " + scriptEngine.getTargets().toString();
        scriptEngine.setTarget(target);
        return "Successfully set target to " + target;
    }


    private Object save2File(String cmd) {
        Date date = new Date();
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss." + scriptEngine.extension());
        String filePath = f.format(date);

        // extract file path if exists
        if (cmd.length() > 5) filePath = cmd.substring(5);

        // save scripts into file
        try {
            File file = new File(filePath);
            if (!file.exists()) { file.createNewFile(); }
            FileWriter fileWriter = new FileWriter(file.getName(), true);
            fileWriter.write(String.join("\n", scripts));
            fileWriter.close();
            return "Successfully saved scripts into " + filePath;
        } catch (IOException e) {
            return "Fail to save scripts into " + filePath + "\n" + e.getMessage();
        }
    }

    private Object help() {
        return "\nThe REPL (Read-Eval-Print-Loop) acts like an interpreter.  " +
                "Valid statements, expressions, and declarations are immediately compiled and executed.\n" +
                "The complete set of commands are also available as described below.  " +
                "Commands must be prefixed with a colon at the REPL prompt (:quit for example.)\n\n" +
                "Commands: \n" +
                "  help                -- Show a list of all available commands.\n" +
                "  clear               -- Clear screen.\n" +
                "  save [filePath]     -- Save scripts into filePath (if provided).\n" +
                "  target              -- Show current interpreter target.\n" +
                "  target [targetName] -- Switch interpreter target to target that 'targetName' specifies. Support targets: "
                + scriptEngine.getTargets().toString() + ", Default: " + scriptEngine.getDefaultTarget() + "\n" +
                "  debug               -- Enter debug mode if you encounter any exception you don't know.\n" +
                "  quit                -- Quit the Yan interpreter.\n" +
                "  quit debug          -- Quit the debug mode.";
    }

    private Object unrecognized() {
        return "Unrecognized command --- Type :help for assistance.";
    }

    public String welcomeMessage() {
        Date date = new Date();
        String msg = "Welcome to " + scriptEngine.name() + " version " + scriptEngine.version() +
                "(default, " + date.toString() + ")" + "\n" +
                "Type :help for assistance.";
        return msg;
    }
}
