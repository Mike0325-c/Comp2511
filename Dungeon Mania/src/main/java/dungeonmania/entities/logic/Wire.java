package dungeonmania.entities.logic;


import java.util.ArrayList;
import java.util.List;

import dungeonmania.entities.Entity;
import dungeonmania.entities.EntityMethods;
import dungeonmania.entities.collectables.Bomb;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class Wire extends Entity {
    private boolean isActived = false;
    private int currTick = 0;
    private int tick = 0;


    public Wire(Position position) {
        super(position);
        setEntityInterface(new EntityMethods());
    }

    public boolean isActived() {
        return isActived;
    }

    public void setActived(boolean isActived) {
        this.isActived = isActived;
         currTick = tick;
    }

    public void addtick() {
        tick += 1;
    }

    public int getCurrTick() {
        return currTick;
    }

    public void triggerBomb(GameMap map) {
        List<Entity> bombList = new ArrayList<Entity>();
        bombList = Logicfactory.countCardinallyAdjacentPosition(this, map);
        for (Entity entity : bombList) {
            if (entity instanceof Bomb) {
                if (isActived) {
                    ((Bomb) entity).explode(map);
                }
            }
        }
    }
}
