package dungeonmania.entities;

import dungeonmania.Game;
import dungeonmania.map.GameMap;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

import java.util.List;
import java.util.UUID;

public abstract class Entity {
    public static final int FLOOR_LAYER = 0;
    public static final int ITEM_LAYER = 1;
    public static final int DOOR_LAYER = 2;
    public static final int CHARACTER_LAYER = 3;

    private Position position;
    private Position previousPosition;
    private Position previousDistinctPosition;
    private Direction facing;
    private String entityId;
    private int stuck;
    private boolean becomeStuck;
    private EntityInterface entityInterface;

    public Entity(Position position) {
        this.position = position;
        this.previousPosition = position;
        this.previousDistinctPosition = null;
        this.entityId = UUID.randomUUID().toString();
        this.facing = null;
        entityInterface = new EntityMethods2();
        stuck = 0;
        becomeStuck = false;
    }

    public void setEntityInterface(EntityInterface entityInterface) {
        this.entityInterface = entityInterface;
    }

    public boolean canMoveOnto(GameMap map, Entity entity) {
        return entityInterface.canMoveOnto(map, entity);
    }

    // use setPosition
    @Deprecated(forRemoval = true)
    public void translate(Direction direction) {
        previousPosition = this.position;
        this.position = Position.translateBy(this.position, direction);
        if (!previousPosition.equals(this.position)) {
            previousDistinctPosition = previousPosition;
        }
    }

    // use setPosition
    @Deprecated(forRemoval = true)
    public void translate(Position offset) {
        this.position = Position.translateBy(this.position, offset);
    }

    public void onOverlap(GameMap map, Entity entity) {
        entityInterface.onOverlap(map, entity);
    }

    public void onMovedAway(GameMap map, Entity entity) {
        entityInterface.onMovedAway(map, entity);
    }

    public void onDestroy(GameMap gameMap) {
        entityInterface.onDestroy(gameMap);
    }

    public Position getPosition() {
        return position;
    }

    public List<Position> getCardinallyAdjacentPositions1() {
        return position.getCardinallyAdjacentPositions();
    }

    public Position getPreviousPosition() {
        return previousPosition;
    }

    public Position getPreviousDistinctPosition() {
        return previousDistinctPosition;
    }

    public String getId() {
        return entityId;
    }

    public void setPosition(Position position) {
        previousPosition = this.position;
        this.position = position;
        if (!previousPosition.equals(this.position)) {
            previousDistinctPosition = previousPosition;
        }
    }

    public void setFacing(Direction facing) {
        this.facing = facing;
    }

    public Direction getFacing() {
        return this.facing;
    }

    public int getStuck() {
        return stuck;
    }

    public void setStuck(int stuck) {
        this.stuck = stuck;
    }

    public boolean isBecomeStuck() {
        return becomeStuck;
    }

    public void setBecomeStuck(boolean becomeStuck) {
        this.becomeStuck = becomeStuck;
    }

    public void checkStepIntoSwarmTile(Game game, Position position) {
        List<Entity> entities = game.getMapEntities(position);
        if (entities != null && entities.size() > 0 && entities.stream().anyMatch(e -> e instanceof SwampTile)) {
            for (Entity e : entities) {
                if (e instanceof SwampTile) {
                    this.setStuck(((SwampTile) e).getMovementFactor());
                }
            }
        }
    }
}
