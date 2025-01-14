package dungeonmania.mvp;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class SwampTileTest {
    @Test
    @Tag("18-1")
    @DisplayName("Test spider affected by swamp")
    public void testSwampSpider() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_spawnSpiderTest_swamp", "c_swampTest");
        assertEquals(1, TestUtils.getEntities(res, "swamp_tile").size());

        // Get the swamp entity
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        // Check the spider is on swamp
        assertEquals(new Position(6, 4), getSpiderPos(res));

        // Check that the position of spider does not change for the duration of
        // movement factor
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(6, 4), getSpiderPos(res));

        // Check that spider now moved out of swamp
        res = dmc.tick(Direction.RIGHT);
        assertFalse(new Position(6, 4).equals(getSpiderPos(res)));
    }

    @Test
    @Tag("18-2")
    @DisplayName("Test player not affected by swamp")
    public void testSwampPlayer() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_swampTest_player", "c_swampTest");
        assertEquals(1, TestUtils.getEntities(res, "swamp_tile").size());

        // Get the swamp entity
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.DOWN);

        // Check the player is on swamp
        assertEquals(new Position(6, 6), getPlayerPos(res));

        // Check that player now moved out of swamp
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(7, 6), getPlayerPos(res));
    }

    @Test
    @Tag("18-3")
    @DisplayName("Test mercenary affected by swamp")
    public void testSwampMercenary() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_swampTest_mercenary", "c_swampTest");
        assertEquals(1, TestUtils.getEntities(res, "swamp_tile").size());

        // Get the swamp entity
        res = dmc.tick(Direction.UP);
        // Check the mercenary is on swamp
        assertEquals(new Position(5, 4), getMerPos(res));

        // Check that the position of mercenary does not change for the duration of
        // movement factor
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.UP);
        assertEquals(new Position(5, 4), getMerPos(res));

        // Check that mercenary now moved out of swamp
        res = dmc.tick(Direction.UP);
        assertFalse(new Position(5, 4).equals(getMerPos(res)));
    }

    @Test
    @Tag("18-4")
    @DisplayName("Test mercenary affected by multiple swamp")
    public void testMultiSwampMercenary() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_swampTest2_mercenary", "c_swampTest");
        assertEquals(2, TestUtils.getEntities(res, "swamp_tile").size());

        // Get the swamp entity
        res = dmc.tick(Direction.UP);
        // Check the spider is on swamp
        assertEquals(new Position(5, 5), getMerPos(res));

        // Check that the position of mercenary does not change for the duration of
        // movement factor
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.DOWN);
        assertEquals(new Position(5, 5), getMerPos(res));

        // Check that mercenary now moved onto another swamp
        res = dmc.tick(Direction.UP);
        assertTrue(new Position(5, 4).equals(getMerPos(res)));

        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.UP);
        assertTrue(new Position(5, 4).equals(getMerPos(res)));

        res = dmc.tick(Direction.DOWN);
        assertFalse(new Position(5, 4).equals(getMerPos(res)));
    }

    @Test
    @Tag("18-5")
    @DisplayName("Test mercenary not affected by swamp")
    public void testSwampMerBribe() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_swampTest_Mercbribe", "c_swampTest");
        assertEquals(1, TestUtils.getEntities(res, "swamp_tile").size());

        String mercId = TestUtils.getEntitiesStream(res, "mercenary").findFirst().get().getId();
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "treasure").size());
        assertEquals(new Position(6, 1), getMerPos(res));

        // achieve bribe
        res = assertDoesNotThrow(() -> dmc.interact(mercId));
        assertEquals(0, TestUtils.getInventory(res, "treasure").size());

        // Get the swamp entity
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        // Check the mercenary is on swamp
        assertEquals(new Position(3, 1), getMerPos(res));

        // Check that the position of mercenary change for the duration of
        // movement factor because of hasBribed
        res = dmc.tick(Direction.UP);
        assertEquals(new Position(2, 1), getMerPos(res));

    }

    private Position getSpiderPos(DungeonResponse res) {
        return TestUtils.getEntities(res, "spider").get(0).getPosition();
    }

    private Position getPlayerPos(DungeonResponse res) {
        return TestUtils.getEntities(res, "player").get(0).getPosition();
    }

    private Position getMerPos(DungeonResponse res) {
        return TestUtils.getEntities(res, "mercenary").get(0).getPosition();
    }
}
