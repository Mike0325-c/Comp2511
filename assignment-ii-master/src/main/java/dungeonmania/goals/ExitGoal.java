package dungeonmania.goals;

import dungeonmania.Game;

public class ExitGoal implements Goal {

    @Override
    public boolean achieved(Game game) {
        if (game.getPlayer() == null)
            return false;
        return game.hasExited();
    }

    @Override
    public String toString(Game game) {
        if (this.achieved(game))
            return "";
        return ":exit";
    }

}
