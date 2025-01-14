package tributary;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class TributaryTest {
    @Test
    public void tributaryTest() {
        Tributary controller = new Tributary();
        controller.createTopic("topic", "Boolean");
        controller.createPartition("topic", "partition1");
        controller.createPartition("topic", "partition2");
        controller.createPartition("topic", "partition3");
        controller.createConsumerGroup("group", "topic");
        controller.createConsumer("group", "con1");
        controller.createProducer("producer1", "Boolean", "Random");
        assertEquals(controller.getProducer("producer1").get("Allocation"), "Random");
        controller.createProducer("producer2", "Boolean", "Manual");
        Boolean value1 = false;
        Boolean value2 = true;
        controller.produceEvent("producer2", "topic", value1, "partition2");
        controller.produceEvent("producer2", "topic", value2, "partition2");
        controller.produceEvent("not existing", "topic", value2, "partition2");
        controller.produceEvent("producer2", "not existing", value2, "partition2");
        controller.produceEvent("producer2", "not existing", value2);
        controller.consumeEvent("not existing", "partition2");
        controller.consumeEvent("con1", "not existing");

        controller.consumeEvent("con1", "partition2");
        assertEquals(controller.getConsumer("con1").get("Event"), "false");

        controller.consumeEvent("con1", "partition2");
        assertEquals(controller.getConsumer("con1").get("Event"), "true");

        controller.consumeEvent("con1", "partition2");
        assertEquals(controller.getConsumer("con1").get("Event"), "");

        controller.consumeEvent("con1", "partition1");
        assertEquals(controller.getConsumer("con1").get("Event"), "");

        controller.produceEvent("producer2", "topic", value1, "partition1");
        controller.consumeEvent("con1", "partition1");
        assertEquals(controller.getConsumer("con1").get("Event"), "false");

        controller.produceEvent("producer1", "topic", value2);
        assertEquals(controller.getConsumer("not exist"), null);
        assertEquals(controller.getConsumerGroup("not exist"), null);
        assertEquals(controller.getTopic("not exist"), null);

        assertEquals(controller.getConsumerGroup("group").get("Topic"), controller.getTopic("topic").get("ID"));
        assertEquals(controller.getTopic("topic").get("Type"), "Boolean");

        controller.createConsumerGroup("group2", "topic");
        controller.createConsumer("group2", "con3");
        controller.createPartition("not exist", "partition3");
        controller.createConsumerGroup("group", "not exist");
        controller.createConsumer("not exist", "con1");
    }
}
