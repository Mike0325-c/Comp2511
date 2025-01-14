package dungeonmania.entities.enemies;

import dungeonmania.util.Position;

public class Hydra extends ZombieToast {
    private double healthIncreaseAmount;
    private double healthIncreaseRate;

    public Hydra(Position position, double health, double attack, double healthIncreaseAmount,
            double healthIncreaseRate) {
        super(position, health, attack);
        this.healthIncreaseAmount = healthIncreaseAmount;
        this.healthIncreaseRate = healthIncreaseRate;
        this.getBattleStatistics().setHydra(this);
    }

    public double getHealthIncreaseAmount() {
        return healthIncreaseAmount;
    }

    public void setHealthIncreaseAmount(double healthIncreaseAmount) {
        this.healthIncreaseAmount = healthIncreaseAmount;
    }

    public double getHealthIncreaseRate() {
        return healthIncreaseRate;
    }

    public void setHealthIncreaseRate(double healthIncreaseRate) {
        this.healthIncreaseRate = healthIncreaseRate;
    }
}
