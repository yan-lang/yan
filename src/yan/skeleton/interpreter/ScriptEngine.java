package yan.skeleton.interpreter;

public interface ScriptEngine {
    Object execute(String statement) throws Exception;

    String getName();

    String getVersion();
}
