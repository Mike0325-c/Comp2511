package dungeonmania.entities.inventory;

import java.util.List;

import dungeonmania.entities.EntityFactory;
import dungeonmania.entities.collectables.SunStone;
import dungeonmania.entities.collectables.Sword;



public class BuildMidnightArmour implements BuildStrategy {

    @Override
    public InventoryItem buildItem(boolean remove, Inventory inventory, String entity, EntityFactory factory) {
        List<SunStone> sunstone = inventory.getEntities(SunStone.class);
        List<Sword> sword = inventory.getEntities(Sword.class);

        if (sword.size() >= 1 && sunstone.size() >= 1) {
            if (remove) {
                inventory.getItems().remove(sword.get(0));
                inventory.getItems().remove(sunstone.get(0));
            }
            return factory.buildMidnightArmour();
        }
        return null;
    }
}
