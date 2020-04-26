package yan.foundation.driver.lang;

/**
 * {@code Compile Target}类似于Maven里的Build Target，一个target可能包含好几个Phase。
 * 常见的Compiler Target包括lex（词法分析），parse（语法分析）等。
 *
 * @param <In>  Target执行任务的输入
 * @param <Out> Target执行任务的输出
 */
public abstract class Target<In, Out> {
    public String name;
    public Phase<In, Out> phase;
    public Formatter<Out> formatter;

    public Target(String name, Phase<In, Out> phase, Formatter<Out> formatter) {
        this.name = name;
        this.phase = phase;
        this.formatter = formatter;
    }
}
