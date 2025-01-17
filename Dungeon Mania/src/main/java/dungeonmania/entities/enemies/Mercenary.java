package dungeonmania.entities.enemies;

import dungeonmania.Game;
import dungeonmania.entities.Entity;
import dungeonmania.entities.Interactable;
import dungeonmania.entities.Player;
import dungeonmania.entities.buildables.Sceptre;
import dungeonmania.entities.collectables.SunStone;
import dungeonmania.entities.collectables.Treasure;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class Mercenary extends Enemy implements Interactable {
    public static final int DEFAULT_BRIBE_AMOUNT = 1;
    public static final int DEFAULT_BRIBE_RADIUS = 1;
    public static final double DEFAULT_ATTACK = 5.0;
    public static final double DEFAULT_HEALTH = 10.0;

    private int bribeAmount = Mercenary.DEFAULT_BRIBE_AMOUNT;
    private int bribeRadius = Mercenary.DEFAULT_BRIBE_RADIUS;
    private boolean allied = false;

    public Mercenary(Position position, double health, double attack, int bribeAmount, int bribeRadius) {
        super(position, health, attack);
        this.bribeAmount = bribeAmount;
        this.bribeRadius = bribeRadius;
    }

    public boolean isAllied() {
        return allied;
    }

    @Override
    public void onOverlap(GameMap map, Entity entity) {
        if (allied)
            return;
        super.onOverlap(map, entity);
    }

    /**
     * check whether the current merc can be bribed
     * @param player
     * @return
     */
    private boolean canBeBribed(Player player) {
        return bribeRadius >= 0
                && (player.countEntityOfType(Treasure.class) - player.countEntityOfType(SunStone.class)) >= bribeAmount;
    }

    /**
     * bribe the merc
     */
    private void bribe(Player player) {
        for (int i = 0; i < bribeAmount; i++) {
            player.use(Treasure.class);
        }

    }

    @Override
    public void interact(Player player, Game game) {
        allied = true;
        bribe(player);
    }

    @Override
    public void move(Game game) {
        Position nextPos;
        if (allied) {
            if (!game.getPlayer().hasMoved()) {
                nextPos = game.getPlayer().getPreviousPosition();
                game.moveTo(this, nextPos);

            } else if (!game.isAdjacentToPlayer(this)) {
                nextPos = game.getMapDijkstra(this);
                game.moveTo(this, nextPos);
                checkStepIntoSwarmTile(game, nextPos);
            }
        } else {
            if (this.getStuck() > 0) {
                this.setStuck(this.getStuck() - 1);
                return;
            }
            // Follow hostile
            nextPos = game.getMapDijkstra(this);
            game.moveTo(this, nextPos);
            checkStepIntoSwarmTile(game, nextPos);
        }
    }

    @Override
    public boolean isInteractable(Player player) {
        return (!allied && canBeBribed(player)) || player.getInventory().getEntities(Sceptre.class).size() != 0;
    }
}
