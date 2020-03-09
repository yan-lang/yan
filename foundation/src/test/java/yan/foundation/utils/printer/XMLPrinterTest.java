package yan.foundation.utils.printer;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class XMLPrinterTest {
    private final XMLPrinter printer = new XMLPrinter("note");
    ClassLoader classLoader = getClass().getClassLoader();

    @Test
    public void testEscape() {
        String out = printer.escape("<test>");
        assertEquals("&lt;test&gt;", out);
    }

    @Test
    public void testException() {
        Assertions.assertThrows(RuntimeException.class, () -> {
            printer.openElement("test");
            String out = printer.flush();
        });
    }

    @Test
    public void testBasic() {
        printer.openElement("text", true);
        printer.pushText("+");
        printer.closeElement();
        printer.pushSimpleElement("type", "ADD");
        printer.pushSimpleElement("line", "1");
        printer.pushSimpleElement("Column", "2");

        String out = printer.flush();
        try (InputStream inputStream = classLoader.getResourceAsStream("printer/XMLPrinterTestCase1.xml")) {
            String result = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
            assertEquals(result, out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testNest() {
        var printer = new XMLPrinter("program");
        printer.openElement("function");
        printer.pushSimpleElement("name", "add");

        printer.openElement("params");

        printer.openElement("param");
        printer.pushSimpleElement("name", "x");
        printer.pushSimpleElement("type", "int");
        printer.closeElement();

        printer.openElement("param");
        printer.pushSimpleElement("name", "y");
        printer.pushSimpleElement("type", "int");
        printer.closeElement();

        printer.closeElement();

        printer.closeElement();

        String out = printer.flush();
        try (InputStream inputStream = classLoader.getResourceAsStream("printer/XMLPrinterTestCase2.xml")) {
            String result = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
            assertEquals(result, out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
