package yan.foundation.driver.lang;

public interface Formatter<T> extends Serializer<T> {
    String format(T t);

    @Override
    default byte[] serialize(T t) {
        return format(t).getBytes();
    }
}
