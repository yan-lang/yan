package yan.foundation.driver.error;

public class Warning extends Unexpected {
    public Warning(int line, int column, String sourceName, String message, String context, String hint) {
        super(line, column, sourceName, message, context, hint);
    }

    @Override
    public String getType() {
        return "warning";
    }
}
