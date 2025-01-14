package tributary;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class PartitionTest {
    @Test
    public void partitionTest() {
        Partition<Integer> intPartition = new Partition<Integer>("01", "Integer");
        Partition<String> strPartition = new Partition<String>("02", "String");
        Partition<Boolean> booPartition = new Partition<Boolean>("03", "Boolean");
        Partition<Integer> intClone = new Partition<>(intPartition);
        assertEquals(intClone.getId(), intPartition.getId());
        assertEquals(intClone.getType(), intPartition.getType());

        Boolean logic = true;
        String string = "string";
        Integer integer = 10;
        Event<Boolean> booleanEvent = new Event<Boolean>(logic);
        Event<Boolean> booleanEvent2 = new Event<Boolean>(logic);
        Event<Boolean> booleanEvent3 = new Event<Boolean>(logic);
        Event<Boolean> booleanEvent4 = new Event<Boolean>(logic);
        Event<String> strEvent = new Event<String>(string);
        Event<Integer> intEvent = new Event<Integer>(integer);

        intPartition.add(intEvent);
        strPartition.add(strEvent);
        booPartition.add(booleanEvent);
        booPartition.add(booleanEvent2);
        booPartition.add(booleanEvent3);
        assertEquals(booPartition.toJSON().get("ID"), "03");

        assertNotEquals(booPartition.getCurrEvent(), booleanEvent2);
        assertTrue(booPartition.getCurrEvent().equals(booleanEvent));
        booPartition.next();
        assertTrue(booPartition.getCurrEvent().equals(booleanEvent2));
        booPartition.next();
        assertTrue(booPartition.getCurrEvent().equals(booleanEvent3));
        booPartition.next();
        assertTrue(booPartition.getCurrEvent() == null);
        booPartition.add(booleanEvent4);
        assertTrue(booPartition.getCurrEvent().equals(booleanEvent4));
    }
}
