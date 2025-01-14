package dungeonmania.entities.buildables;

import dungeonmania.Game;
import dungeonmania.battles.BattleStatistics;


public class Sceptre extends Buildable {
    private int duration;

    public Sceptre(int duration) {
        super(null);
        this.duration = duration;
    }

    @Override
    public void use(Game game) {
        return;
    }

    @Override
    public BattleStatistics applyBuff(BattleStatistics origin) {
       return origin;
    }

    @Override
    public int getDurability() {
        return 0;
    }

    public int getDuration() {
        return duration;
    }
}
