package tributary;

public class ManualProducer<E> extends Producer<E> {

    public ManualProducer(String id, String type) {
        super(id, type);
    }

    public void produce(Event<E> event, Topic<E> topic, String key) {
        event.setProducer(this);
        synchronized (this) {
            Partition<E> partition = topic.getPartition(key);
            partition.add(event);
        }
    }

    @Override
    public String getClassName() {
        return "Manual";
    }
}
