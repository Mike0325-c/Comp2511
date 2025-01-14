package dungeonmania.entities.logic;


import java.util.List;

import dungeonmania.entities.Entity;
import dungeonmania.entities.Switch;
import dungeonmania.map.GameMap;



public class XorLogic implements LogicInterface {

    @Override
    public boolean canLogic(Entity entity, GameMap map) {
        int count = 0;
        List<Entity> adjacentEntities = Logicfactory.countCardinallyAdjacentPosition(entity, map);
        if (entity instanceof Switch && adjacentEntities.contains((Switch) entity)) {
            adjacentEntities.remove((Switch) entity);
        }

        for (Entity entities: adjacentEntities) {
            if (entities instanceof Switch) {
                if (((Switch) entities).isActivated()) {
                    count += 1;
                }
            }
            if (entities instanceof Wire) {
                if (((Wire) entities).isActived()) {
                    count += 1;
                }
            }
        }

        if (count == 1) {
            return true;
        }

        return false;
    }
}


