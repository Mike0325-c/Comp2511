package dungeonmania.entities.collectables.bombstate;

import java.util.List;
import java.util.stream.Collectors;

import dungeonmania.entities.Entity;
import dungeonmania.entities.Switch;
import dungeonmania.entities.collectables.Bomb;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class InventoryState implements BombState {
    private Bomb bomb;

    public InventoryState(Bomb bomb) {
        this.bomb = bomb;
    }

    @Override
    public void onPutDown(GameMap map, Position p) {
        bomb.translate(Position.calculatePositionBetween(bomb.getPosition(), p));
        map.addEntity(bomb);
        bomb.setBombState(new PlacedState(bomb));
        List<Position> adjPosList = bomb.getCardinallyAdjacentPositions1();
        adjPosList.stream().forEach(node -> {
            List<Entity> entities = map.getEntities(node)
                                        .stream()
                                        .filter(e -> (e instanceof Switch))
                                        .collect(Collectors.toList());
            entities.stream()
                    .map(Switch.class::cast)
                    .forEach(s -> s.subscribe(bomb, map));
            entities.stream()
                    .map(Switch.class::cast)
                    .forEach(s -> bomb.subscribe(s));
        });
    }


    @Override
    public void onOverlap(GameMap map, Entity entity, List<Switch> subs) {
        return;
    }
}
