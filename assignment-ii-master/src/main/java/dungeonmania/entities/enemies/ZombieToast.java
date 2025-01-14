package dungeonmania.entities.enemies;

import dungeonmania.Game;

import dungeonmania.util.Position;

public class ZombieToast extends Enemy {
    public static final double DEFAULT_HEALTH = 5.0;
    public static final double DEFAULT_ATTACK = 6.0;

    public ZombieToast(Position position, double health, double attack) {
        super(position, health, attack);
    }

    @Override
    public void move(Game game) {
        if (this.getStuck() > 0) {
            this.setStuck(this.getStuck() - 1);
            return;
        }
        setMoveStrategy(new MoveRandom());
        Position nextPos = getMoveStrategy().moveway(game, this);
        game.moveTo(this, nextPos);
        this.checkStepIntoSwarmTile(game, nextPos);
    }
}
