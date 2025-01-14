package tributary;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONObject;

public class ConsumerGroup<E> {
    private List<Consumer<E>> consumers = new ArrayList<>();
    private String id;
    private Topic<E> topic;
    private String type;

    public ConsumerGroup(String id, Topic<E> topic) {
        this.id = id;
        this.topic = topic;
        topic.subscribe(this);
        this.type = topic.getType();
    }

    public String getType() {
        return type;
    }

    public Consumer<E> getConsumer(String id) {
        return consumers.stream().filter(e -> e.getId().equals(id)).findAny().orElse(null);
    }

    public String getId() {
        return id;
    }

    public void add(Consumer<E> consumer) {
        if (consumers.size() == 0) {
            for (Partition<E> partition : topic.getPartitions()) {
                consumer.subscribe(partition);
            }
        }
        consumers.add(consumer);
    }

    public Consumer<E> getRandomConsumer() {
        Random rand = new Random();
        Consumer<E> consumer = consumers.get(rand.nextInt(consumers.size()));
        return consumer;
    }

    public JSONObject toJSON() {
        JSONObject consumerGroup = new JSONObject();
        JSONArray consumerArray = new JSONArray();
        consumerGroup.put("ID", id);
        consumerGroup.put("Topic", topic.getId());
        for (Consumer<E> consumer : consumers) {
            consumerArray.put(consumer.toJSON());
        }
        consumerGroup.put("Consumers", consumerArray);
        return consumerGroup;
    }
}
