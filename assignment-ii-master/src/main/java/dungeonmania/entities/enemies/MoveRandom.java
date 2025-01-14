package dungeonmania.entities.enemies;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import dungeonmania.Game;
import dungeonmania.entities.Entity;
import dungeonmania.util.Position;

public class MoveRandom implements MoveStrategy {

    @Override
    public Position moveway(Game game, Entity entity) {
        Position nextPos;
        Random randGen = new Random();
        List<Position> pos = entity.getCardinallyAdjacentPositions1();
        pos = pos
            .stream()
            .filter(p -> game.gameCanMoveTo(entity, p)).collect(Collectors.toList());
        if (pos.size() == 0) {
            nextPos = entity.getPosition();
        } else {
            nextPos = pos.get(randGen.nextInt(pos.size()));
        }
        return nextPos;
    }
}
