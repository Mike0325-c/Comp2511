package dungeonmania.entities.logic;

import dungeonmania.entities.Entity;
import dungeonmania.entities.Player;
import dungeonmania.entities.enemies.Spider;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class SwitchDoor extends Entity {
    private boolean open = false;
    private LogicInterface logicInterface;
    public SwitchDoor(Position position, String logicField) {
        super(position.asLayer(Entity.DOOR_LAYER));
        this.logicInterface = Logicfactory.logicCreate(logicField);
    }

    @Override
    public boolean canMoveOnto(GameMap map, Entity entity) {
        return (entity instanceof Player && open) || entity instanceof Spider;
    }

    @Override
    public void onOverlap(GameMap map, Entity entity) {
        if (!(entity instanceof Player))
            return;
    }


    public void setOpen(GameMap map) {
        this.open = logicInterface.canLogic(this, map);
    }
}
