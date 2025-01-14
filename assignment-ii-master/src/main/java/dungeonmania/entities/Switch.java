package dungeonmania.entities;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.entities.collectables.Bomb;
import dungeonmania.entities.logic.Logicfactory;
import dungeonmania.entities.logic.Wire;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class Switch extends Entity {
    private boolean activated;
    private List<Bomb> bombs = new ArrayList<>();
    private int currTick = 0;
    private int tick = 0;

    public Switch(Position position) {
        super(position.asLayer(Entity.ITEM_LAYER));
        setEntityInterface(new EntityMethods());
    }

    public void subscribe(Bomb b) {
        bombs.add(b);
    }

    public void subscribe(Bomb bomb, GameMap map) {
        bombs.add(bomb);
        if (activated) {
            bombs.stream().forEach(b -> b.notify(map));
        }
    }

    public void unsubscribe(Bomb b) {
        bombs.remove(b);
    }


    @Override
    public void onOverlap(GameMap map, Entity entity) {
        if (entity instanceof Boulder) {
            activated = true;
            currTick = tick;
            bombs.stream().forEach(b -> b.notify(map));
        }
    }

    @Override
    public void onMovedAway(GameMap map, Entity entity) {
        if (entity instanceof Boulder) {
            activated = false;
        }
    }

    public boolean isActivated() {
        return activated;
    }

    public List<Bomb> getBombs() {
        return bombs;
    }

    public void addtick() {
        tick += 1;
    }

    public int getCurrTick() {
        return currTick;
    }

    public void triggerWire(GameMap map) {
        List<Entity> wireList = new ArrayList<Entity>();
        if (activated) {
            wireList = Logicfactory.countCardinallyAdjacentPosition(this, map);
            for (Entity entity : wireList) {
                if (entity instanceof Wire) {
                    List<Entity> wireList2 = Logicfactory.countCardinallyAdjacentPosition(entity, map);
                    triggerWires(entity, map, wireList2);
                }
            }
        } else {
            wireList = Logicfactory.countCardinallyAdjacentPosition(this, map);
            for (Entity entity : wireList) {
                if (entity instanceof Wire) {
                    List<Entity> wireList2 = Logicfactory.countCardinallyAdjacentPosition(entity, map);
                    triggerDownWires(entity, map, wireList2);
                }
            }
        }
    }

    public void triggerWires(Entity wire, GameMap map, List<Entity> wireList) {
        ((Wire) wire).setActived(true);
        for (Entity entity : wireList) {
            if (entity instanceof Wire) {
                List<Entity> wireList2 = Logicfactory.countCardinallyAdjacentPosition(entity, map);
                wireList2.remove(wire);
                triggerWires(entity, map, wireList2);
            }
        }
    }

    public void triggerDownWires(Entity wire, GameMap map, List<Entity> wireList) {
        ((Wire) wire).setActived(false);
        for (Entity entity : wireList) {
            if (entity instanceof Wire) {
                List<Entity> wireList2 = Logicfactory.countCardinallyAdjacentPosition(entity, map);
                wireList2.remove(wire);
                triggerDownWires(entity, map, wireList2);
            }
        }
    }
}
