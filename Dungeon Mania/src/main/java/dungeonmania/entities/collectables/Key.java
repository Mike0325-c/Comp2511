package dungeonmania.entities.collectables;

import dungeonmania.entities.CollectMethods;
import dungeonmania.entities.EntityMethods;
import dungeonmania.entities.inventory.InventoryItem;
import dungeonmania.util.Position;

public class Key extends CollectMethods implements InventoryItem {
    private int number;

    public Key(Position position, int number) {
        super(position);
        this.number = number;
        setEntityInterface(new EntityMethods());
    }


    public int getnumber() {
        return number;
    }

}
