package dungeonmania.goals;

import dungeonmania.Game;

public class EnemyGoal implements Goal {
    private int numEnemies;

    public EnemyGoal(int numEnemies) {
        this.numEnemies = numEnemies;
    }

    @Override
    public boolean achieved(Game game) {
        if (game.getPlayer() == null)
            return false;
        return game.hasDestroyedEnemies(numEnemies) && game.hasDestroyedAllSpawners();

    }

    @Override
    public String toString(Game game) {
        if (this.achieved(game))
            return "";
        return ":enemies";
    }
}
