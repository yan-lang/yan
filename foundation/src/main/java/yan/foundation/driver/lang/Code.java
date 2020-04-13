package yan.foundation.driver.lang;

public class Code {
    public String filename;
    public String content;

    public Code(String filename, String content) {
        this.filename = filename;
        this.content = content;
    }

    public static Code of(String filename, String content) {
        return new Code(filename, content);
    }
}
