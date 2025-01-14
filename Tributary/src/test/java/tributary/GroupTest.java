package tributary;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class GroupTest {
    @Test
    public void groupTest() {
        Boolean logic = true;
        Event<Boolean> booleanEvent = new Event<Boolean>(logic);
        Event<Boolean> booleanEvent2 = new Event<Boolean>(logic);
        Event<Boolean> booleanEvent3 = new Event<Boolean>(logic);
        Partition<Boolean> booPartition = new Partition<Boolean>("p1", "Boolean");
        Partition<Boolean> booPartition2 = new Partition<Boolean>("p2", "Boolean");
        booPartition.add(booleanEvent);
        booPartition.add(booleanEvent2);
        booPartition.add(booleanEvent3);
        booPartition2.add(booleanEvent);
        booPartition2.add(booleanEvent2);

        Topic<Boolean> topic = new Topic<>("t1", "Boolean");
        topic.add(booPartition);
        topic.add(booPartition2);

        Consumer<Boolean> consumer = new Consumer<Boolean>("c1", "Boolean");
        Consumer<Boolean> consumer2 = new Consumer<Boolean>("c2", "Boolean");
        ConsumerGroup<Boolean> group = new ConsumerGroup<>("g1", topic);
        group.add(consumer2);
        group.add(consumer);
        assertEquals(group.toJSON().get("ID"), "g1");

        assertTrue(group.getConsumer("c1").equals(consumer));
        Consumer<Boolean> random = group.getRandomConsumer();
        assertTrue(random.equals(consumer) || random.equals(consumer2));
    }
}
