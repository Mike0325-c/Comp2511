package tributary;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class Topic<E> {
    private List<Partition<E>> partitions = new ArrayList<>();
    private List<ConsumerGroup<E>> consumerGroups = new ArrayList<>();
    private String id;
    private String type;

    public Topic(String id, String type) {
        this.id = id;
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public Partition<E> getPartition(String id) {
        return partitions.stream().filter(e -> e.getId().equals(id)).findAny().orElse(null);
    }

    public Boolean hasPartition(String id) {
        return partitions.stream().anyMatch(e -> e.getId().equals(id));
    }

    public void add(Partition<E> partition) {
        partitions.add(partition);
        for (ConsumerGroup<E> consumerGroup : consumerGroups) {
            Consumer<E> consumer = consumerGroup.getRandomConsumer();
            if (consumer != null) {
                consumer.subscribe(partition);
            }
        }
    }

    public void subscribe(ConsumerGroup<E> consumerGroup) {
        consumerGroups.add(consumerGroup);
    }

    public List<Partition<E>> getPartitions() {
        List<Partition<E>> list = new ArrayList<Partition<E>>(partitions);
        return list;
    }

    public JSONObject toJSON() {
        JSONObject topic = new JSONObject();
        JSONArray partitions = new JSONArray();
        for (Partition<E> partition : this.partitions) {
            partitions.put(partition.toJSON());
        }
        topic.put("ID", id);
        topic.put("Type", type);
        topic.put("Partitions", partitions);
        return topic;
    }

    public String getId() {
        return id;
    }
}
