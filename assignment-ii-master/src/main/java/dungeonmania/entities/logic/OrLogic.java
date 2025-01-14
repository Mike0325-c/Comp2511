package dungeonmania.entities.logic;


import java.util.List;

import dungeonmania.entities.Entity;
import dungeonmania.entities.Switch;
import dungeonmania.map.GameMap;

public class OrLogic implements LogicInterface {

    @Override
    public boolean canLogic(Entity entity, GameMap map) {
        List<Entity> adjacentEntities = Logicfactory.countCardinallyAdjacentPosition(entity, map);
        if (entity instanceof Switch && adjacentEntities.contains((Switch) entity)) {
            adjacentEntities.remove((Switch) entity);
        }

        for (Entity entities: adjacentEntities) {
            if (entities instanceof Switch) {
                if (((Switch) entities).isActivated()) {
                    return true;
                }
            }
            if (entities instanceof Wire) {
                if (((Wire) entities).isActived()) {
                    return true;
                }
            }
        }
        return false;
    }
}

