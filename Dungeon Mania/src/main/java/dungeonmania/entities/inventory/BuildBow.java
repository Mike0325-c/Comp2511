package dungeonmania.entities.inventory;

import java.util.List;

import dungeonmania.entities.EntityFactory;
import dungeonmania.entities.collectables.Arrow;
import dungeonmania.entities.collectables.Wood;

public class BuildBow implements BuildStrategy {

    @Override
    public InventoryItem buildItem(boolean remove, Inventory inventory, String entity, EntityFactory factory) {
        List<Wood> wood = inventory.getEntities(Wood.class);
        List<Arrow> arrows = inventory.getEntities(Arrow.class);

        if (wood.size() >= 1 && arrows.size() >= 3) {
            if (remove) {
                inventory.getItems().remove(wood.get(0));
                inventory.getItems().remove(arrows.get(0));
                inventory.getItems().remove(arrows.get(1));
                inventory.getItems().remove(arrows.get(2));
            }
            return factory.buildBow();
        }
        return null;
    }

}
