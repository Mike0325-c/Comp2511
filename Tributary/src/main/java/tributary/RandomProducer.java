package tributary;

import java.util.List;
import java.util.Random;

public class RandomProducer<E> extends Producer<E> {

    public RandomProducer(String id, String type) {
        super(id, type);
    }

    public void produce(Event<E> event, Topic<E> topic, String key) {
        Random rand = new Random();
        event.setProducer(this);
        synchronized (this) {
            List<Partition<E>> list = topic.getPartitions();
            Partition<E> partition = list.get(rand.nextInt(list.size()));
            partition.add(event);
        }
    }

    @Override
    public String getClassName() {
        return "Random";
    }
}
