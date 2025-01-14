package tributary;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

public class EventTest {
    @Test
    public void eventTest() {
        Boolean logic = true;
        String string = "string";
        Integer integer = 10;
        List<String> list = new ArrayList<>();
        Event<Boolean> booleanEvent = new Event<Boolean>(logic);
        Event<String> strEvent = new Event<String>(string);
        Event<Integer> intEvent = new Event<Integer>(integer);
        Event<List<String>> listEvent = new Event<List<String>>(list, "partition");
        assertTrue(booleanEvent.getType().equals("Boolean"));
        assertTrue(strEvent.getType().equals("String"));
        assertTrue(intEvent.getType().equals("Integer"));
        assertTrue(listEvent.getType().equals("ArrayList"));
        assertEquals(strEvent.toJSON().get("Value"), "string");
    }
}
