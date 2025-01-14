package tributary;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class TopicTest {
    @Test
    public void topicTest() {
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

        assertEquals(topic.getPartition("5"), null);
        assertEquals(topic.getPartition("p1"), booPartition);
        assertEquals(topic.getPartition("p2"), booPartition2);
        assertEquals(topic.toJSON().get("ID"), "t1");
    }
}
