package dungeonmania.entities.logic;

import dungeonmania.entities.Entity;
import dungeonmania.entities.EntityMethods;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class LightBulb extends Entity {
    private LogicInterface logicInterface;
    private boolean hastriggered = false;
    public LightBulb(Position position, String logicField) {
        super(position);
        this.logicInterface = Logicfactory.logicCreate(logicField);
        setEntityInterface(new EntityMethods());
    }

    public boolean checkTriggered() {
        return hastriggered;
    }

    public void setTriggered(GameMap map) {
        this.hastriggered = logicInterface.canLogic(this, map);
    }
}
