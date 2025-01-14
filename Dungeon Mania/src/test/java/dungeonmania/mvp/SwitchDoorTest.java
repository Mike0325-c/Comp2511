package dungeonmania.mvp;

import dungeonmania.DungeonManiaController;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SwitchDoorTest {

    @Test
    @Tag("4-1")
    @DisplayName("Test player cannot walk through a closed switch door ")
    public void cannotWalkClosedDoor() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame(
            "d_DoorsKeysTest_cannotWalkCloseswitchdDoor", "c_DoorsKeysTest_cannotWalkClosedDoor");

            Position pos = TestUtils.getEntities(res, "player").get(0).getPosition();

        // try to walk through switch door and fail
        res = dmc.tick(Direction.RIGHT);
        assertEquals(pos, TestUtils.getEntities(res, "player").get(0).getPosition());
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.LEFT);


        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);

        pos = TestUtils.getEntities(res, "player").get(0).getPosition();
        res = dmc.tick(Direction.UP);
        //can walk through the switch door
        assertNotEquals(pos, TestUtils.getEntities(res, "player").get(0).getPosition());
    }


    @Test
    @Tag("4-1")
    @DisplayName("Test switch door And")
    public void switchDoorAnd() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame(
            "d_DoorsKeysTest_DoorAnd", "c_DoorsKeysTest_cannotWalkClosedDoor");

            Position pos = TestUtils.getEntities(res, "player").get(0).getPosition();


        res = dmc.tick(Direction.RIGHT);
        assertNotEquals(pos, TestUtils.getEntities(res, "player").get(0).getPosition());
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.DOWN);

        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);

        pos = TestUtils.getEntities(res, "player").get(0).getPosition();
        res = dmc.tick(Direction.LEFT);
        //can walk through the switch door
        assertNotEquals(pos, TestUtils.getEntities(res, "player").get(0).getPosition());
    }



        @Test
        @Tag("4-1")
        @DisplayName("Test switch Xor ")
        public void switchDoorXor() {
            DungeonManiaController dmc;
            dmc = new DungeonManiaController();
            DungeonResponse res = dmc.newGame(
                "d_DoorsKeysTest_DoorXor", "c_DoorsKeysTest_cannotWalkClosedDoor");

            Position pos = TestUtils.getEntities(res, "player").get(0).getPosition();


            res = dmc.tick(Direction.RIGHT);
            assertNotEquals(pos, TestUtils.getEntities(res, "player").get(0).getPosition());
            res = dmc.tick(Direction.DOWN);
            res = dmc.tick(Direction.RIGHT);
            res = dmc.tick(Direction.RIGHT);
            pos = TestUtils.getEntities(res, "player").get(0).getPosition();
            res = dmc.tick(Direction.UP);

            assertNotEquals(pos, TestUtils.getEntities(res, "player").get(0).getPosition());
            res = dmc.tick(Direction.RIGHT);
            res = dmc.tick(Direction.UP);
            res = dmc.tick(Direction.UP);
            res = dmc.tick(Direction.UP);
            res = dmc.tick(Direction.LEFT);
            res = dmc.tick(Direction.DOWN);
            res = dmc.tick(Direction.RIGHT);
            res = dmc.tick(Direction.DOWN);
            res = dmc.tick(Direction.DOWN);
            pos = TestUtils.getEntities(res, "player").get(0).getPosition();
            res = dmc.tick(Direction.LEFT);

            assertEquals(pos, TestUtils.getEntities(res, "player").get(0).getPosition());
        }



        @Test
        @Tag("4-1")
        @DisplayName("Test switch coand ")
        public void switchDoorCoand() {
            DungeonManiaController dmc;
            dmc = new DungeonManiaController();
            DungeonResponse res = dmc.newGame(
                "d_DoorsKeysTest_DoorcoAnd", "c_DoorsKeysTest_cannotWalkClosedDoor");

            Position pos = TestUtils.getEntities(res, "player").get(0).getPosition();

            res = dmc.tick(Direction.RIGHT);
            assertNotEquals(pos, TestUtils.getEntities(res, "player").get(0).getPosition());
            res = dmc.tick(Direction.DOWN);
            res = dmc.tick(Direction.RIGHT);
            res = dmc.tick(Direction.RIGHT);
            res = dmc.tick(Direction.RIGHT);
            pos = TestUtils.getEntities(res, "player").get(0).getPosition();
            res = dmc.tick(Direction.UP);

            assertNotEquals(pos, TestUtils.getEntities(res, "player").get(0).getPosition());
    }







}
