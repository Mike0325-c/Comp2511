package dungeonmania.entities;

import dungeonmania.entities.enemies.Enemy;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class SwampTile extends Entity {
    private int movementFactor;

    public SwampTile(Position position, int movementFactor) {
        super(position);
        this.movementFactor = movementFactor;
        setEntityInterface(new EntityMethods());
    }

    public int getMovementFactor() {
        return movementFactor;
    }

    public void setMovementFactor(int movementFactor) {
        this.movementFactor = movementFactor;
    }

    @Override
    public boolean canMoveOnto(GameMap map, Entity entity) {
        return entity instanceof Player || entity instanceof Enemy;
    }
}
