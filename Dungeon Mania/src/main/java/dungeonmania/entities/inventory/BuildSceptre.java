package dungeonmania.entities.inventory;

import java.util.List;

import dungeonmania.entities.EntityFactory;
import dungeonmania.entities.collectables.Arrow;
import dungeonmania.entities.collectables.Key;
import dungeonmania.entities.collectables.SunStone;
import dungeonmania.entities.collectables.Treasure;
import dungeonmania.entities.collectables.Wood;

public class BuildSceptre implements BuildStrategy {
    @Override
    public InventoryItem buildItem(boolean remove, Inventory inventory, String entity, EntityFactory factory) {
        List<Wood> wood = inventory.getEntities(Wood.class);
        List<Treasure> treasure = inventory.getEntities(Treasure.class);
        List<Key> keys = inventory.getEntities(Key.class);
        List<SunStone> sunstone = inventory.getEntities(SunStone.class);
        List<Arrow> arrows = inventory.getEntities(Arrow.class);

        if ((wood.size() >= 1 || arrows.size() >= 2)
        && (((treasure.size() >= 1 || keys.size() >= 1) && sunstone.size() >= 1) || (sunstone.size() >= 2))) {
            if (remove) {
                if (wood.size() >= 1) {
                    inventory.getItems().remove(wood.get(0));
                } else if (arrows.size() >= 2) {
                    inventory.getItems().remove(arrows.get(0));
                    inventory.getItems().remove(arrows.get(1));
                }

                if (sunstone.size() >= 2) {
                    inventory.getItems().remove(sunstone.get(0));
                } else if (sunstone.size() == 1 && keys.size() >= 1) {
                    inventory.getItems().remove(keys.get(0));
                    inventory.getItems().remove(sunstone.get(0));
                } else if (sunstone.size() == 1 && treasure.size() >= 1) {
                    inventory.getItems().remove(treasure.get(0));
                    inventory.getItems().remove(sunstone.get(0));
                }
            }
            return factory.buildSceptre();
        }
        return null;
    }
}
