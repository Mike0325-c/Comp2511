package tributary;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

public class Tributary {
    private List<Topic<?>> topics = new ArrayList<>();
    private List<Producer<?>> producers = new ArrayList<>();
    private List<ConsumerGroup<?>> groups = new ArrayList<>();
    private List<Partition<?>> partitions = new ArrayList<>();
    private List<Consumer<?>> consumers = new ArrayList<>();

    /*
     * Create a topic with given type, ids has to be unique among all topics
     */
    public void createTopic(String id, String type) {
        topics.add(new Topic<>(id, type));
    }

    /*
     * Create a partition in a given topic, ids has to be unique among all partitions
     */
    public void createPartition(String topicId, String id) {
        Topic<?> topic = topics.stream().filter(e -> e.getId().equals(topicId)).findAny().orElse(null);
        if (topic != null) {
            topic.add(new Partition<>(id, topic.getType()));
            partitions.add(topic.getPartition(id));
        }
    }

    /*
     * Create a consumer group subscribing a given topic, ids has to be unique among all groups
     */
    public void createConsumerGroup(String id, String topicId) {
        Topic<?> topic = topics.stream().filter(e -> e.getId().equals(topicId)).findAny().orElse(null);
        if (topic != null) {
            groups.add(new ConsumerGroup<>(id, topic));
        }
    }

    /*
     * Create a consumer inside a given consumer group, ids has to be unique among all consumers
     */
    public void createConsumer(String groupId, String id) {
        ConsumerGroup<?> group = groups.stream().filter(e -> e.getId().equals(groupId)).findAny().orElse(null);
        if (group != null) {
            group.add(new Consumer<>(id, group.getType()));
            consumers.add(group.getConsumer(id));
        }
    }

    /*
     * Create a producer with given allocation and type, ids has to be unique among all producers
     */
    public void createProducer(String id, String type, String allocation) {
        switch (allocation) {
            case "Random":
                producers.add(new RandomProducer<>(id, type));
            case "Manual":
                producers.add(new ManualProducer<>(id, type));
            default:
        }
    }

    /*
     * Produce an event of any type by given producer's allocation assign to a given partition in a topic
     * Would do nothing if the types are not matching
     */
    public <E> void produceEvent(String producerId, String topicId, E event, String partitionId) {
        Producer<?> producer = producers.stream().filter(e -> e.getId().equals(producerId)).findAny().orElse(null);
        Topic<?> topic = topics.stream().filter(e -> e.getId().equals(topicId)).findAny().orElse(null);
        Event<E> newEvent = new Event<E>(event, partitionId);
        if (producer != null && topic != null && newEvent.getType().equals(topic.getType())
            && newEvent.getType().equals(producer.getType()) && topic.hasPartition(partitionId)) {
            ((Producer<E>) producer).produce(newEvent, ((Topic<E>) topic), partitionId);
        }
    }

    /*
     * Produce an event of any type by given producer's allocation assign to a random partition in a topic
     * Would do nothing if the types are not matching
     */
    public <E> void produceEvent(String producerId, String topicId, E event) {
        Producer<?> producer = producers.stream().filter(e -> e.getId().equals(producerId)).findAny().orElse(null);
        Topic<?> topic = topics.stream().filter(e -> e.getId().equals(topicId)).findAny().orElse(null);
        Event<E> newEvent = new Event<E>(event);
        if (producer != null && topic != null && newEvent.getType().equals(topic.getType())
            && newEvent.getType().equals(producer.getType())) {
            ((Producer<E>) producer).produce(newEvent, ((Topic<E>) topic), null);
        }
    }

    /*
     * Consumer consume an event from a given partition start where it last consumed
     */
    public void consumeEvent(String consumerId, String partitionId) {
        Consumer<?> consumer = consumers.stream().filter(e -> e.getId().equals(consumerId)).findAny().orElse(null);
        if (consumer != null && consumer.havePartition(partitionId)) {
            consumer.consume(partitionId);
        }
    }

    /*
     * Get a topic as an JSONOBject with following id
     */
    public JSONObject getTopic(String id) {
        Topic<?> topic = topics.stream().filter(e -> e.getId().equals(id)).findAny().orElse(null);
        if (topic != null) {
            return topic.toJSON();
        }
        return null;
    }

    /*
     * Get a partition as an JSONOBject with following id
     */
    public JSONObject getPartition(String id) {
        Partition<?> partition = partitions.stream().filter(e -> e.getId().equals(id)).findAny().orElse(null);
        if (partition != null) {
            return partition.toJSON();
        }
        return null;
    }

    /*
     * Get a consumer group as an JSONOBject with following id
     */
    public JSONObject getConsumerGroup(String id) {
        ConsumerGroup<?> group = groups.stream().filter(e -> e.getId().equals(id)).findAny().orElse(null);
        if (group != null) {
            return group.toJSON();
        }
        return null;
    }

    /*
     * Get a consumer as an JSONOBject with following id
     */
    public JSONObject getConsumer(String id) {
        Consumer<?> consumer = consumers.stream().filter(e -> e.getId().equals(id)).findAny().orElse(null);
        if (consumer != null) {
            return consumer.toJSON();
        }
        return null;
    }

    /*
     * Get a producer as an JSONOBject with following id
     */
    public JSONObject getProducer(String id) {
        Producer<?> producer = producers.stream().filter(e -> e.getId().equals(id)).findAny().orElse(null);
        if (producer != null) {
            return producer.toJSON();
        }
        return null;
    }
}
