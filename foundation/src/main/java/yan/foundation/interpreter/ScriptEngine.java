package yan.foundation.interpreter;

public interface ScriptEngine {
    Object execute(String statement) throws Exception;

    String getName();

    String getVersion();
}
