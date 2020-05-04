package yan.foundation.driver.lang;

public interface Serializer<T> {
    byte[] serialize(T t);
}
