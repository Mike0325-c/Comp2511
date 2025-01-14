package tributary;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class Consumer<E> {
    private List<Partition<E>> partitions = new ArrayList<>();
    private List<Event<E>> consumedEvents = new ArrayList<>();
    private String currPartition = "";
    private String currEvent = "";
    private String id;
    private String type;

    public Consumer(String id, String type) {
        this.id = id;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public void subscribe(Partition<E> partition) {
        partitions.add(new Partition<>(partition));
    }

    public boolean havePartition(String id) {
        return partitions.stream().anyMatch(e -> e.getId().equals(id));
    }

    public void consume(String id) {
        Partition<E> partition = partitions.stream().filter(e -> e.getId().equals(id)).findAny().orElse(null);
        currPartition = partition.getId();
        currEvent = "";
        synchronized (this) {
            if (partition.getCurrEvent() != null) {
                currEvent = partition.getCurrEvent().getValue().toString();
                consumedEvents.add(partition.getCurrEvent());
            }
            partition.next();
        }
    }

    public Event<E> getCurrEvent() {
        return consumedEvents.stream().filter(e -> e.getValue().toString().equals(currEvent)).findAny().orElse(null);
    }

    public Partition<E> getCurrPartition() {
        return partitions.stream().filter(e -> e.getId().equals(currPartition)).findAny().orElse(null);
    }

    public JSONObject toJSON() {
        JSONObject consumer = new JSONObject();
        JSONArray par = new JSONArray();
        consumer.put("ID", id);
        for (Partition<E> partition : partitions) {
            par.put(partition.toJSON());
        }
        consumer.put("Partition", par);
        consumer.put("Event", currEvent);
        return consumer;
    }
}
