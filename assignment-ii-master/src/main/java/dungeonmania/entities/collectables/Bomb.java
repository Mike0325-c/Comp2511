package dungeonmania.entities.collectables;

import dungeonmania.util.Position;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import dungeonmania.entities.CollectMethods;
import dungeonmania.entities.Entity;
import dungeonmania.entities.EntityMethods;
import dungeonmania.entities.Player;
import dungeonmania.entities.Switch;
import dungeonmania.entities.collectables.bombstate.BombState;
import dungeonmania.entities.collectables.bombstate.SpawnedState;
import dungeonmania.entities.inventory.InventoryItem;
import dungeonmania.map.GameMap;

public class Bomb extends CollectMethods implements InventoryItem {


    public static final int DEFAULT_RADIUS = 1;
    private int radius;
    private BombState bombState;

    private List<Switch> subs = new ArrayList<>();

    public Bomb(Position position, int radius) {
        super(position);
        this.bombState = new SpawnedState(this);
        this.radius = radius;
        setEntityInterface(new EntityMethods());
    }

    public void subscribe(Switch s) {
        this.subs.add(s);
    }

    public void notify(GameMap map) {
        explode(map);
    }


    @Override
    public void onOverlap(GameMap map, Entity entity) {
        bombState.onOverlap(map, entity, subs);
    }

    public void onPutDown(GameMap map, Position p) {
       bombState.onPutDown(map, p);
    }

    /**
     * it destroys all entities in diagonally and cardinally adjacent cells, except for the player
     * @param map
     */
    public void explode(GameMap map) {
        int x = getPosition().getX();
        int y = getPosition().getY();
        for (int i = x - radius; i <= x + radius; i++) {
            for (int j = y - radius; j <= y + radius; j++) {
                List<Entity> entities = map.getEntities(new Position(i, j));
                entities = entities.stream()
                    .filter(e -> !(e instanceof Player))
                    .collect(Collectors.toList());
                for (Entity e: entities) map.destroyEntity(e);
            }
        }
    }

    public void setBombState(BombState bombState) {
        this.bombState = bombState;
    }
}
