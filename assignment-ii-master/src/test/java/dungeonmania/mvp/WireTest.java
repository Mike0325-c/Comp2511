package dungeonmania.mvp;

import dungeonmania.DungeonManiaController;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

import dungeonmania.util.Position;

public class WireTest {


    @Test
    @Tag("5-6")
    @DisplayName("Test walk onto a wire")
    public void walkOntoWire() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame(
            "d_Wire_light_test", "c_DoorsKeysTest_pickUpSunStone");

        assertEquals(4, TestUtils.getEntities(res, "wire").size());
        assertEquals(0, TestUtils.getInventory(res, "wire").size());


        Position pos = TestUtils.getEntities(res, "player").get(0).getPosition();

        res = dmc.tick(Direction.RIGHT);
        //any entity can walk onto wire
        assertNotEquals(pos, TestUtils.getEntities(res, "player").get(0).getPosition());
        //wire can not be collected
        assertEquals(4, TestUtils.getEntities(res, "wire").size());
        assertEquals(0, TestUtils.getInventory(res, "wire").size());
    }

    @Test
    @Tag("5-6")
    @DisplayName("Test wire trigger logic entities")
    public void wireTrigger() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame(
            "d_Wire_light_trigger", "c_DoorsKeysTest_pickUpSunStone");

        assertEquals(3, TestUtils.getEntities(res, "wire").size());
        assertEquals(0, TestUtils.getInventory(res, "wire").size());


        assertEquals(3, TestUtils.getEntities(res, "light_bulb_off").size());
        assertEquals(0, TestUtils.getInventory(res, "light_bulb_off").size());
        res = dmc.tick(Direction.RIGHT);

        //active all light bulb
        assertEquals(0, TestUtils.getEntities(res, "light_bulb_off").size());
        assertEquals(3, TestUtils.getEntities(res, "light_bulb_on").size());

    }



    @Test
    @Tag("5-6")
    @DisplayName("Test wire trigger logic entities")
    public void wireTrigger2() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame(
            "d_Wire_light_trigger", "c_DoorsKeysTest_pickUpSunStone");

        assertEquals(3, TestUtils.getEntities(res, "wire").size());
        assertEquals(0, TestUtils.getInventory(res, "wire").size());


        assertEquals(3, TestUtils.getEntities(res, "light_bulb_off").size());
        assertEquals(0, TestUtils.getInventory(res, "light_bulb_off").size());
        res = dmc.tick(Direction.RIGHT);

        //active all light bulb
        assertEquals(0, TestUtils.getEntities(res, "light_bulb_off").size());
        assertEquals(3, TestUtils.getEntities(res, "light_bulb_on").size());

    }
}
