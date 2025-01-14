package dungeonmania.entities.inventory;

import java.util.List;

import dungeonmania.entities.EntityFactory;
import dungeonmania.entities.collectables.Key;
import dungeonmania.entities.collectables.SunStone;
import dungeonmania.entities.collectables.Treasure;
import dungeonmania.entities.collectables.Wood;

public class BuildShield implements BuildStrategy {

    @Override
    public InventoryItem buildItem(boolean remove, Inventory inventory, String entity, EntityFactory factory) {
        List<Wood> wood = inventory.getEntities(Wood.class);
        List<Treasure> treasure = inventory.getEntities(Treasure.class);
        List<Key> keys = inventory.getEntities(Key.class);
        List<SunStone> sunstone = inventory.getEntities(SunStone.class);

        if (wood.size() >= 2 && (treasure.size() >= 1 || keys.size() >= 1 || sunstone.size() >= 1)) {
            if (remove) {
                inventory.getItems().remove(wood.get(0));
                inventory.getItems().remove(wood.get(1));
                if (sunstone.size() >= 1) {
                    inventory.getItems().remove(null);
                } else if (treasure.size() >= 1) {
                    inventory.getItems().remove(treasure.get(0));
                } else {
                    inventory.getItems().remove(keys.get(0));
                }
            }
            return factory.buildShield();
        }
        return null;
    }
}
