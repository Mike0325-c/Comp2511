package dungeonmania.entities.logic;

import dungeonmania.entities.Entity;
import dungeonmania.map.GameMap;

public interface LogicInterface {
    public boolean canLogic(Entity entity, GameMap map);
}
