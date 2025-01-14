package dungeonmania.entities.logic;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.entities.Entity;
import dungeonmania.entities.Switch;
import dungeonmania.map.GameMap;

public class CoAndlogic implements LogicInterface {

    @Override
    public boolean canLogic(Entity entity, GameMap map) {
        boolean sameTick = true;
        int count = 0;
        List<Integer> tickCheck = new ArrayList<Integer>();
        List<Entity> adjacentEntities = Logicfactory.countCardinallyAdjacentPosition(entity, map);

        if (adjacentEntities.size() < 2) {
            return false;
        }

        for (Entity entities: adjacentEntities) {
            if (entities instanceof Switch) {
                if (!((Switch) entities).isActivated()) {
                    return false;
                }
                count += 1;
                tickCheck.add(((Switch) entities).getCurrTick());
            }
            if (entities instanceof Wire) {
                if (!((Wire) entities).isActived()) {
                    return false;
                }
                count += 1;
                tickCheck.add(((Wire) entities).getCurrTick());
            }
        }


        if (tickCheck.size() > 0) {
            for (Integer integer : tickCheck) {
                if (tickCheck.get(0) != integer) {
                    sameTick = false;
                }
            }
        }

        if (count < 2 || !sameTick) {
            return false;
        }
        return true;
    }

}
