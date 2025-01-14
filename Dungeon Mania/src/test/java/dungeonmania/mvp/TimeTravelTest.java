package dungeonmania.mvp;

import static org.junit.jupiter.api.Assertions.assertEquals;

import dungeonmania.util.Position;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;

public class TimeTravelTest {

    @Test
    @Tag("20-1")
    @DisplayName("Test player can pick up time_turner")
    public void canPickupTimeTurner() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame(
                "d_timeTurnerTest", "c_generateTest");

        assertEquals(1, TestUtils.getEntities(res, "time_turner").size());
        Position pos = TestUtils.getEntities(res, "player").get(0).getPosition();
        // successful to pick up time_turner
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        assertEquals(0, TestUtils.getEntities(res, "time_turner").size());
    }
}
