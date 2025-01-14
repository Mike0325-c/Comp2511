package tributary;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class ProducerTest {
    @Test
    public void producerTest() {
        Producer<Object> random = new RandomProducer<>("random", "Object");
        Producer<Object> manual = new ManualProducer<>("manual", "Object");
        assertEquals(random.toJSON().get("Allocation"), "Random");
        assertEquals(manual.toJSON().get("Allocation"), "Manual");

        Partition<Object> partition = new Partition<Object>("p", "Object");
        Topic<Object> topic = new Topic<Object>("topic", "Object");
        topic.add(partition);
        assertEquals(partition.getEvents().size(), 0);

        Object value = "object";
        Event<Object> event = new Event<Object>(value);
        random.produce(event, topic, "okay");
        assertEquals(partition.getEvents().size(), 1);

        manual.produce(event, topic, "p");
        assertEquals(partition.getEvents().size(), 2);
    }
}
