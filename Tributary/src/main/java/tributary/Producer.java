package tributary;

import org.json.JSONObject;

public abstract class Producer<E> {
    private String id;
    private String type;

    public Producer(String id, String type) {
        this.id = id;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public JSONObject toJSON() {
        JSONObject producer = new JSONObject();
        producer.put("ID", id);
        producer.put("Type", type);
        producer.put("Allocation", getClassName());
        return producer;
    }

    public abstract String getClassName();
    public abstract void produce(Event<E> event, Topic<E> topic, String key);
}
