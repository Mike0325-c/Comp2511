package dungeonmania.entities.collectables;

import dungeonmania.entities.CollectMethods;
import dungeonmania.entities.EntityMethods;
import dungeonmania.entities.inventory.InventoryItem;
import dungeonmania.util.Position;

public class Wood extends CollectMethods implements InventoryItem {
    public Wood(Position position) {
        super(position);
        setEntityInterface(new EntityMethods());
    }
}
