package dungeonmania.entities.collectables.bombstate;

import java.util.List;

import dungeonmania.entities.Entity;
import dungeonmania.entities.Switch;
import dungeonmania.entities.collectables.Bomb;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class PlacedState implements BombState {
    private Bomb bomb;


    public PlacedState(Bomb bomb) {
        this.bomb = bomb;
    }


    @Override
    public void onPutDown(GameMap map, Position p) {
        return;
    }


    @Override
    public void onOverlap(GameMap map, Entity entity, List<Switch> subs) {
        return;
    }
}
