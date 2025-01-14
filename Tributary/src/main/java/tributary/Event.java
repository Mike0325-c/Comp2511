package tributary;

import java.time.LocalDate;
import java.util.UUID;

import org.json.JSONObject;

public class Event<E> {
    private LocalDate date;
    private String id;
    private Producer<E> source;
    private String key;
    private E value;
    private String type;

    public Event(E value) {
        this.value = value;
        this.date = java.time.LocalDate.now();
        this.id = UUID.randomUUID().toString();
        this.type = value.getClass().getSimpleName();
    }

    public Event(E value, String key) {
        this.value = value;
        this.key = key;
        this.date = java.time.LocalDate.now();
        this.id = UUID.randomUUID().toString();
        this.type = value.getClass().getSimpleName();
    }

    public void setProducer(Producer<E> producer) {
        this.source = producer;
    }

    public String getType() {
        return type;
    }

    public E getValue() {
        return value;
    }

    public JSONObject toJSON() {
        JSONObject event = new JSONObject();
        JSONObject header = new JSONObject();
        header.put("Datetime created", date.toString());
        header.put("ID", id.toString());
        header.put("Payload Type", type.getClass().getSimpleName());
        if (source != null) {
            header.put("Source", source.toJSON());
        }
        event.put("Headers", header);
        event.put("Key", key);
        event.put("Value", value.toString());
        return event;
    }
}
