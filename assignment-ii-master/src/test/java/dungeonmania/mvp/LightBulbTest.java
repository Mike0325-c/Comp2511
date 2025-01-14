package dungeonmania.mvp;

import dungeonmania.DungeonManiaController;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LightBulbTest {
    @Test
    @Tag("12-1")
    @DisplayName("Test Light bulbs cannot be collected")
    public void testLightBulbCollect() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame(
            "d_lightCollect", "c_DoorsKeysTest_canOpenDoorWithsunStone");


        assertEquals(1, TestUtils.getEntities(res, "light_bulb_off").size());
        assertEquals(0, TestUtils.getInventory(res, "light_bulb_off").size());

        res = dmc.tick(Direction.LEFT);
        //Light bulbs cannot be collected
        assertEquals(1, TestUtils.getEntities(res, "light_bulb_off").size());
        assertEquals(0, TestUtils.getInventory(res, "light_bulb_off").size());

    }



    @Test
    @Tag("12-1")
    @DisplayName("Test turn on a light bulb with or")
    public void testLightBulbOR() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame(
            "d_lightOrTurnon", "c_DoorsKeysTest_canOpenDoorWithsunStone");


        assertEquals(1, TestUtils.getEntities(res, "light_bulb_off").size());
        assertEquals(0, TestUtils.getInventory(res, "light_bulb_off").size());
        res = dmc.tick(Direction.RIGHT);
        //turn on the light bulb
        assertEquals(0, TestUtils.getEntities(res, "light_bulb_off").size());
        assertEquals(1, TestUtils.getEntities(res, "light_bulb_on").size());
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.UP);

        //turn off the light bulb
        assertEquals(1, TestUtils.getEntities(res, "light_bulb_off").size());
        assertEquals(0, TestUtils.getEntities(res, "light_bulb_on").size());

    }

    @Test
    @Tag("12-1")
    @DisplayName("Test turn on a light bulb with and")
    public void testLightBulbAnd() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame(
            "d_lightAndTurnon", "c_DoorsKeysTest_canOpenDoorWithsunStone");


        assertEquals(1, TestUtils.getEntities(res, "light_bulb_off").size());
        assertEquals(0, TestUtils.getInventory(res, "light_bulb_off").size());
        res = dmc.tick(Direction.RIGHT);

        //can not turn on the light if there is just one cardinally adjacent activated conductors
        assertEquals(1, TestUtils.getEntities(res, "light_bulb_off").size());
        assertEquals(0, TestUtils.getEntities(res, "light_bulb_on").size());


        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.RIGHT);

        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.UP);

        //turn on the light bulb
        assertEquals(0, TestUtils.getEntities(res, "light_bulb_off").size());

        assertEquals(1, TestUtils.getEntities(res, "light_bulb_on").size());

    }


    @Test
    @Tag("12-1")
    @DisplayName("Test turn on a light bulb with and")
    public void testLightBulbAnd2() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame(
            "d_lightAndTurnon2", "c_DoorsKeysTest_canOpenDoorWithsunStone");


        assertEquals(1, TestUtils.getEntities(res, "light_bulb_off").size());
        assertEquals(0, TestUtils.getInventory(res, "light_bulb_off").size());
        res = dmc.tick(Direction.RIGHT);
        //can not turn on the light if there is just one cardinally adjacent activated conductors
        assertEquals(1, TestUtils.getEntities(res, "light_bulb_off").size());
        assertEquals(0, TestUtils.getEntities(res, "light_bulb_on").size());


        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.RIGHT);

        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.UP);

        //can not turn on if there is a entity which is not actived
        assertEquals(1, TestUtils.getEntities(res, "light_bulb_off").size());

        assertEquals(0, TestUtils.getEntities(res, "light_bulb_on").size());

        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.LEFT);


        //turn on the light bulb
        assertEquals(1, TestUtils.getEntities(res, "light_bulb_on").size());
        assertEquals(0, TestUtils.getEntities(res, "light_bulb_off").size());

    }


    @Test
    @Tag("12-1")
    @DisplayName("Test turn on a light bulb with xor")
    public void testLightBulbXor() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame(
            "d_lightXorTurnon", "c_DoorsKeysTest_canOpenDoorWithsunStone");


        assertEquals(1, TestUtils.getEntities(res, "light_bulb_off").size());
        assertEquals(0, TestUtils.getInventory(res, "light_bulb_off").size());
        res = dmc.tick(Direction.RIGHT);

        //turn on the light if there is just one cardinally adjacent activated conductors
        assertEquals(0, TestUtils.getEntities(res, "light_bulb_off").size());
        assertEquals(1, TestUtils.getEntities(res, "light_bulb_on").size());


        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.RIGHT);

        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.UP);

        //2 cardinally adjacent activated conductors, the light bulb will turn off
        assertEquals(1, TestUtils.getEntities(res, "light_bulb_off").size());
        assertEquals(0, TestUtils.getEntities(res, "light_bulb_on").size());

    }


    @Test
    @Tag("12-1")
    @DisplayName("Test turn on a light bulb with coand")
    public void testLightBulbCoand() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame(
            "d_lightcoandTurnon", "c_DoorsKeysTest_canOpenDoorWithsunStone");


        assertEquals(1, TestUtils.getEntities(res, "light_bulb_off").size());
        assertEquals(0, TestUtils.getInventory(res, "light_bulb_off").size());
        res = dmc.tick(Direction.RIGHT);

        //not turn on the light
        assertEquals(0, TestUtils.getEntities(res, "light_bulb_off").size());
        assertEquals(1, TestUtils.getEntities(res, "light_bulb_on").size());

    }

}
