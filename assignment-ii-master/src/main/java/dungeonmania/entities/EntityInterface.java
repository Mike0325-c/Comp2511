package dungeonmania.entities;

import dungeonmania.map.GameMap;

public interface EntityInterface {
    public void onOverlap(GameMap map, Entity entity);

    public void onMovedAway(GameMap map, Entity entity);

    public void onDestroy(GameMap gameMap);

    public boolean canMoveOnto(GameMap map, Entity entity);
}
