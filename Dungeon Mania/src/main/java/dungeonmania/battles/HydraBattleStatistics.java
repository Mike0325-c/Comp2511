package dungeonmania.battles;

import dungeonmania.entities.enemies.Hydra;

public class HydraBattleStatistics extends BattleStatistics {
    private Hydra hydra;

    public HydraBattleStatistics(double health, double attack, double defence, double attackMagnifier,
            double damageReducer, Hydra hydra) {
        super(health, attack, defence, attackMagnifier, damageReducer);
        this.hydra = hydra;
    }

    public HydraBattleStatistics(double health, double attack, double defence, double attackMagnifier,
            double damageReducer, boolean isInvincible, boolean isEnabled, Hydra hydra) {
        super(health, attack, defence, attackMagnifier, damageReducer, isInvincible, isEnabled);
        this.hydra = hydra;
    }
}
