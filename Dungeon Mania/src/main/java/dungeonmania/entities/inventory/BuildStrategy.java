package dungeonmania.entities.inventory;
import dungeonmania.entities.EntityFactory;

//Stratege pattern
public interface BuildStrategy {
    public InventoryItem buildItem(boolean remove, Inventory inventory, String entity, EntityFactory factory);
}
