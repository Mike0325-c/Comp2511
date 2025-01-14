package tributary;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

public class Partition<E> {
    private List<Event<E>> events = new ArrayList<>();
    private List<Partition<E>> obsevers = new ArrayList<>();
    private int index = 0;
    private String id;
    private String type;

    public Partition(String id, String type) {
        this.id = id;
        this.type = type;
    }

    public Partition(Partition<E> partition) {
        this.id = partition.getId();
        this.type = partition.getType();
        this.events = partition.getEvents();
        partition.subscribe(this);
    }

    public String getType() {
        return type;
    }

    public void subscribe(Partition<E> partition) {
        obsevers.add(partition);
    }

    public void next() {
        index++;
        if (index >= events.size()) {
            index = events.size();
        }
    }

    public String getId() {
        return id;
    }

    public void add(Event<E> event) {
        events.add(event);
        for (Partition<E> partition : obsevers) {
            partition.add(event);
        }
    }

    public List<Event<E>> getEvents() {
        List<Event<E>> newEvents = new ArrayList<>(events);
        return newEvents;
    }

    public Event<E> getCurrEvent() {
        if (index >= events.size()) {
            return null;
        }
        return events.get(index);
    }

    public JSONObject toJSON() {
        JSONObject partition = new JSONObject();
        JSONObject eventsList = new JSONObject();
        for (Event<E> event : events) {
            eventsList.put("Event", event.toJSON());
        }
        partition.put("ID", id);
        partition.put("Events", eventsList);
        return partition;
    }
}
