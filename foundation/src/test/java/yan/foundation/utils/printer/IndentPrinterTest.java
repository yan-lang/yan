package yan.foundation.utils.printer;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IndentPrinterTest {
    ClassLoader classLoader = getClass().getClassLoader();

    @Test
    public void testBasic() {
        IndentPrinter printer = new IndentPrinter(4);
        printer.println("Token");
        printer.indent();
        printer.println("text: +").println("type: ADD").println("line: 1").println("column: 2");
        printer.print("index:").print(" ").formatln("%d", 4);
        printer.unindent();
        printer.print("/Token");
        String out = printer.flush();

        try (InputStream inputStream = classLoader.getResourceAsStream("printer/IdentPrinterTestCase1.txt")) {
            String result = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
            assertEquals(out, result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
