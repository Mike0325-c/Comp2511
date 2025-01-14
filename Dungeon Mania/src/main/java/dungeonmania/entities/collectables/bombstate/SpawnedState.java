package dungeonmania.entities.collectables.bombstate;

import java.util.List;

import dungeonmania.entities.Entity;
import dungeonmania.entities.Player;
import dungeonmania.entities.Switch;
import dungeonmania.entities.collectables.Bomb;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class SpawnedState implements BombState {
    private Bomb bomb;

    public SpawnedState(Bomb bomb) {
        this.bomb = bomb;
    }

    @Override
    public void onPutDown(GameMap map, Position p) {
        return;
    }

    @Override
    public void onOverlap(GameMap map, Entity entity, List<Switch> subs) {
        if (entity instanceof Player) {
            entity.onOverlap(map, bomb);
            subs.stream().forEach(s -> s.unsubscribe(bomb));
        }
        bomb.setBombState(new InventoryState(bomb));
    }
}
