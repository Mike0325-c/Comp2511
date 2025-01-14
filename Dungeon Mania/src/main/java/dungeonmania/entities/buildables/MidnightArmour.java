package dungeonmania.entities.buildables;

import dungeonmania.Game;
import dungeonmania.battles.BattleStatistics;


public class MidnightArmour extends Buildable {
    private int midnightArmourAttack;
    private int midnightArmourDefence;
    public MidnightArmour(int midnightArmourAttack, int midnightArmourDefence) {
        super(null);
        this.midnightArmourAttack = midnightArmourAttack;
        this.midnightArmourDefence = midnightArmourDefence;
    }

    @Override
    public void use(Game game) {
        return;
    }

    @Override
    public BattleStatistics applyBuff(BattleStatistics origin) {
        return BattleStatistics.applyBuff(origin, new BattleStatistics(
            0,
            midnightArmourAttack,
            midnightArmourDefence,
            1,
            1));
    }

    @Override
    public int getDurability() {
        return 0;
    }
}
