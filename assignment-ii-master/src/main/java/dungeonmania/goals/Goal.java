package dungeonmania.goals;

import dungeonmania.Game;

public interface Goal {

    public abstract boolean achieved(Game game);

    public abstract String toString(Game game);
}
