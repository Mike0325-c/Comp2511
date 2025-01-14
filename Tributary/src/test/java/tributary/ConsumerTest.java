package tributary;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class ConsumerTest {
    @Test
    public void consumerTest() {
        Consumer<Integer> intConsumer = new Consumer<Integer>("01", "Integer");
        Consumer<String> strConsumer = new Consumer<String>("02", "String");
        Consumer<Boolean> booConsumer = new Consumer<Boolean>("03", "Boolean");
        assertTrue(booConsumer.getType().equals("Boolean"));
        assertTrue(strConsumer.getType().equals("String"));
        assertTrue(intConsumer.getType().equals("Integer"));
    }
}
