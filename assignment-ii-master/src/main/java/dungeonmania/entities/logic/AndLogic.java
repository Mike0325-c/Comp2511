package dungeonmania.entities.logic;

import java.util.List;

import dungeonmania.entities.Entity;
import dungeonmania.entities.Switch;
import dungeonmania.map.GameMap;


public class AndLogic implements LogicInterface {
    @Override
    public boolean canLogic(Entity entity, GameMap map) {
        int count = 0;
        List<Entity> adjacentEntities = Logicfactory.countCardinallyAdjacentPosition(entity, map);

        if (adjacentEntities.size() < 2) {
            return false;
        }

        for (Entity entities: adjacentEntities) {
            if (entities instanceof Switch) {
                count += 1;
                if (!((Switch) entities).isActivated()) {
                    return false;
                }
            }
            if (entities instanceof Wire) {
                count += 1;
                if (!((Wire) entities).isActived()) {
                    return false;
                }
            }
        }
        if (count < 2) {
            return false;
        }
        return true;
    }
}
