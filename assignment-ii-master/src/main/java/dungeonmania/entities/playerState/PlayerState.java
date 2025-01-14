package dungeonmania.entities.playerState;

import dungeonmania.battles.BattleStatistics;

public interface PlayerState {

    public void transitionInvisible();

    public void transitionInvincible();

    public void transitionBase();

    public BattleStatistics applyBuff(BattleStatistics origin);
}
