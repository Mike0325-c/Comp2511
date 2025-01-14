package dungeonmania.entities.collectables.bombstate;

import java.util.List;

import dungeonmania.entities.Entity;
import dungeonmania.entities.Switch;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public interface BombState {
    public void onPutDown(GameMap map, Position p);
    public void onOverlap(GameMap map, Entity entity, List<Switch> subs);
}
