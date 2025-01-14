package dungeonmania.entities;

import dungeonmania.map.GameMap;


public class EntityMethods implements EntityInterface {

    @Override
    public void onOverlap(GameMap map, Entity entity) {
        return;
    }

    @Override
    public void onMovedAway(GameMap map, Entity entity) {
       return;
    }

    @Override
    public void onDestroy(GameMap gameMap) {
      return;
    }

    @Override
    public boolean canMoveOnto(GameMap map, Entity entity) {
        return true;
    }
}
