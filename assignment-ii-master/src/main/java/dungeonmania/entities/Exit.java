package dungeonmania.entities;

import dungeonmania.util.Position;

public class Exit extends Entity {
    public Exit(Position position) {
        super(position.asLayer(Entity.ITEM_LAYER));
        setEntityInterface(new EntityMethods());
    }
}
