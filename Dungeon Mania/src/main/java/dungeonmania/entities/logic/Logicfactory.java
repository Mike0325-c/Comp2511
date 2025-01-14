package dungeonmania.entities.logic;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.entities.Entity;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class Logicfactory {

    public static LogicInterface logicCreate(String logic) {
        switch (logic) {
            case "or":
                return new OrLogic();
            case "and":
                return new AndLogic();
            case "xor":
                return new XorLogic();
            case "co_and":
                return new CoAndlogic();
            default:
                return null;
        }
    }


    public static List<Entity> countCardinallyAdjacentPosition(Entity entity, GameMap map) {
        List<Position> positionList = entity.getCardinallyAdjacentPositions1();
        List<Entity> adjacentEntities = new ArrayList<Entity>();
        for (Position position : positionList) {
            if (map.getEntities(position).size() != 0) {
                adjacentEntities.addAll(map.getEntities(position));
            }
        }
        return adjacentEntities;
    }



}
