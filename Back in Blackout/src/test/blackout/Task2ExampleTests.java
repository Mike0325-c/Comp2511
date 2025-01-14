package blackout;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import unsw.blackout.BlackoutController;
import unsw.blackout.FileTransferException;
import unsw.response.models.FileInfoResponse;
import unsw.response.models.EntityInfoResponse;
import unsw.utils.Angle;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static unsw.utils.MathsHelper.RADIUS_OF_JUPITER;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static blackout.TestHelpers.assertListAreEqualIgnoringOrder;

@TestInstance(value = Lifecycle.PER_CLASS)
public class Task2ExampleTests {
    @Test
    public void testEntitiesInRange() {
        // Task 2
        // Example from the specification
        BlackoutController controller = new BlackoutController();

        // Creates 1 satellite and 2 devices
        // Gets a device to send a file to a satellites and gets another device to download it.
        // StandardSatellites are slow and transfer 1 byte per minute.
        controller.createSatellite("Satellite1", "StandardSatellite", 1000 + RADIUS_OF_JUPITER, Angle.fromDegrees(320));
        controller.createSatellite("Satellite2", "StandardSatellite", 1000 + RADIUS_OF_JUPITER, Angle.fromDegrees(315));
        controller.createDevice("DeviceB", "LaptopDevice", Angle.fromDegrees(310));
        controller.createDevice("DeviceC", "HandheldDevice", Angle.fromDegrees(320));
        controller.createDevice("DeviceD", "HandheldDevice", Angle.fromDegrees(180));
        controller.createSatellite("Satellite3", "StandardSatellite", 2000 + RADIUS_OF_JUPITER, Angle.fromDegrees(175));

        assertListAreEqualIgnoringOrder(Arrays.asList("DeviceC", "Satellite2"),
         controller.communicableEntitiesInRange("Satellite1"));
        assertListAreEqualIgnoringOrder(Arrays.asList("DeviceB", "DeviceC", "Satellite1"),
         controller.communicableEntitiesInRange("Satellite2"));
        assertListAreEqualIgnoringOrder(Arrays.asList("Satellite2"), controller.communicableEntitiesInRange("DeviceB"));

        assertListAreEqualIgnoringOrder(Arrays.asList("DeviceD"), controller.communicableEntitiesInRange("Satellite3"));
    }

    @Test
    public void testSomeExceptionsForSend() {
        // just some of them... you'll have to test the rest
        BlackoutController controller = new BlackoutController();

        // Creates 1 satellite and 2 devices
        // Gets a device to send a file to a satellites and gets another device to download it.
        // StandardSatellites are slow and transfer 1 byte per minute.
        controller.createSatellite("Satellite1", "StandardSatellite", 5000 + RADIUS_OF_JUPITER, Angle.fromDegrees(320));
        controller.createDevice("DeviceB", "LaptopDevice", Angle.fromDegrees(310));
        controller.createDevice("DeviceC", "HandheldDevice", Angle.fromDegrees(320));

        String msg = "Hey";
        controller.addFileToDevice("DeviceC", "FileAlpha", msg);
        assertThrows(FileTransferException.VirtualFileNotFoundException.class,
         () -> controller.sendFile("NonExistentFile", "DeviceC", "Satellite1"));

        assertDoesNotThrow(() -> controller.sendFile("FileAlpha", "DeviceC", "Satellite1"));
        assertEquals(new FileInfoResponse("FileAlpha", "", msg.length(),
         false), controller.getInfo("Satellite1").getFiles().get("FileAlpha"));
        controller.simulate(msg.length() * 2);
        assertThrows(FileTransferException.VirtualFileAlreadyExistsException.class,
         () -> controller.sendFile("FileAlpha", "DeviceC", "Satellite1"));
    }

    @Test
    public void testMovement() {
        // Task 2
        // Example from the specification
        BlackoutController controller = new BlackoutController();

        // Creates 1 satellite and 2 devices
        // Gets a device to send a file to a satellites and gets another device to download it.
        // StandardSatellites are slow and transfer 1 byte per minute.
        controller.createSatellite("Satellite1", "StandardSatellite", 100 + RADIUS_OF_JUPITER, Angle.fromDegrees(340));
        assertEquals(new EntityInfoResponse("Satellite1",
         Angle.fromDegrees(340), 100 + RADIUS_OF_JUPITER, "StandardSatellite"), controller.getInfo("Satellite1"));
        controller.simulate();
        assertEquals(new EntityInfoResponse("Satellite1",
         Angle.fromDegrees(337.95), 100 + RADIUS_OF_JUPITER, "StandardSatellite"), controller.getInfo("Satellite1"));
    }

    @Test
    public void testExample() {
        // Task 2
        // Example from the specification
        BlackoutController controller = new BlackoutController();

        // Creates 1 satellite and 2 devices
        // Gets a device to send a file to a satellites and gets another device to download it.
        // StandardSatellites are slow and transfer 1 byte per minute.
        controller.createSatellite("Satellite1", "StandardSatellite",
         10000 + RADIUS_OF_JUPITER, Angle.fromDegrees(320));
        controller.createDevice("DeviceB", "LaptopDevice", Angle.fromDegrees(310));
        controller.createDevice("DeviceC", "HandheldDevice", Angle.fromDegrees(320));

        String msg = "Hey";
        controller.addFileToDevice("DeviceC", "FileAlpha", msg);
        assertDoesNotThrow(() -> controller.sendFile("FileAlpha", "DeviceC", "Satellite1"));
        assertEquals(new FileInfoResponse("FileAlpha", "",
         msg.length(), false), controller.getInfo("Satellite1").getFiles().get("FileAlpha"));

        controller.simulate(msg.length() * 2);
        assertEquals(new FileInfoResponse("FileAlpha", msg,
         msg.length(), true), controller.getInfo("Satellite1").getFiles().get("FileAlpha"));

        assertDoesNotThrow(() -> controller.sendFile("FileAlpha", "Satellite1", "DeviceB"));
        assertEquals(new FileInfoResponse("FileAlpha", "",
         msg.length(), false), controller.getInfo("DeviceB").getFiles().get("FileAlpha"));

        controller.simulate(msg.length());
        assertEquals(new FileInfoResponse("FileAlpha",
         msg, msg.length(), true), controller.getInfo("DeviceB").getFiles().get("FileAlpha"));

        // Hints for further testing:
        // - What about checking about the progress of the message half way through?
        // - Device/s get out of range of satellite
        // ... and so on.
    }

    @Test
    public void testRelayMovement() {
        // Task 2
        // Example from the specification
        BlackoutController controller = new BlackoutController();

        // Creates 1 satellite and 2 devices
        // Gets a device to send a file to a satellites and gets another device to download it.
        // StandardSatellites are slow and transfer 1 byte per minute.
        controller.createSatellite("Satellite1", "RelaySatellite", 100 + RADIUS_OF_JUPITER,
                                Angle.fromDegrees(180));

        // moves in negative direction
        assertEquals(
                        new EntityInfoResponse("Satellite1", Angle.fromDegrees(180), 100 + RADIUS_OF_JUPITER,
                                        "RelaySatellite"),
                        controller.getInfo("Satellite1"));
        controller.simulate();
        assertEquals(new EntityInfoResponse("Satellite1", Angle.fromDegrees(178.77), 100 + RADIUS_OF_JUPITER,
                        "RelaySatellite"), controller.getInfo("Satellite1"));
        controller.simulate();
        assertEquals(new EntityInfoResponse("Satellite1", Angle.fromDegrees(177.54), 100 + RADIUS_OF_JUPITER,
                        "RelaySatellite"), controller.getInfo("Satellite1"));
        controller.simulate();
        assertEquals(new EntityInfoResponse("Satellite1", Angle.fromDegrees(176.31), 100 + RADIUS_OF_JUPITER,
                        "RelaySatellite"), controller.getInfo("Satellite1"));

        controller.simulate(5);
        assertEquals(new EntityInfoResponse("Satellite1", Angle.fromDegrees(170.18), 100 + RADIUS_OF_JUPITER,
                        "RelaySatellite"), controller.getInfo("Satellite1"));
        controller.simulate(24);
        assertEquals(new EntityInfoResponse("Satellite1", Angle.fromDegrees(140.72), 100 + RADIUS_OF_JUPITER,
                        "RelaySatellite"), controller.getInfo("Satellite1"));
        // edge case
        controller.simulate();
        assertEquals(new EntityInfoResponse("Satellite1", Angle.fromDegrees(139.49), 100 + RADIUS_OF_JUPITER,
                        "RelaySatellite"), controller.getInfo("Satellite1"));
        // coming back
        controller.simulate(1);
        assertEquals(new EntityInfoResponse("Satellite1", Angle.fromDegrees(140.72), 100 + RADIUS_OF_JUPITER,
                        "RelaySatellite"), controller.getInfo("Satellite1"));
        controller.simulate(5);
        assertEquals(new EntityInfoResponse("Satellite1", Angle.fromDegrees(146.85), 100 + RADIUS_OF_JUPITER,
                        "RelaySatellite"), controller.getInfo("Satellite1"));
    }

    @Test
    public void testTeleportingMovement() {
        // Test for expected teleportation movement behaviour
        BlackoutController controller = new BlackoutController();

        controller.createSatellite("Satellite1", "TeleportingSatellite", 10000 + RADIUS_OF_JUPITER,
                        Angle.fromDegrees(0));

        controller.simulate();
        Angle clockwiseOnFirstMovement = controller.getInfo("Satellite1").getPosition();
        controller.simulate();
        Angle clockwiseOnSecondMovement = controller.getInfo("Satellite1").getPosition();
        assertTrue(clockwiseOnSecondMovement.compareTo(clockwiseOnFirstMovement) == 1);

        // It should take 250 simulations to reach theta = 180.
        // Simulate until Satellite1 reaches theta=180
        controller.simulate(250);

        // Verify that Satellite1 is now at theta=0
        assertTrue(controller.getInfo("Satellite1").getPosition().toDegrees() % 360 == 0);
    }


//addtional tests
    @Test
    public void testStandardMovement2() {
        BlackoutController controller = new BlackoutController();

        controller.createSatellite("Satellite1", "StandardSatellite", 1000 + RADIUS_OF_JUPITER, Angle.fromDegrees(10));
        assertEquals(
            new EntityInfoResponse("Satellite1", Angle.fromDegrees(10), 1000 + RADIUS_OF_JUPITER, "StandardSatellite"),
            controller.getInfo("Satellite1")
        );
        controller.simulate();
        assertEquals(
            new EntityInfoResponse("Satellite1",
                Angle.fromDegrees(7.98),
                1000 + RADIUS_OF_JUPITER,
                "StandardSatellite"),
            controller.getInfo("Satellite1")
        );

        controller.simulate(20);
        assertEquals(
            new EntityInfoResponse("Satellite1",
                Angle.fromDegrees(327.58),
                1000 + RADIUS_OF_JUPITER,
                "StandardSatellite"),
            controller.getInfo("Satellite1")
        );

        controller.simulate(60);
        assertEquals(
            new EntityInfoResponse("Satellite1",
                Angle.fromDegrees(206.3808),
                1000 + RADIUS_OF_JUPITER,
                "StandardSatellite"),
            controller.getInfo("Satellite1")
        );

        controller.simulate(120);
        assertEquals(
            new EntityInfoResponse("Satellite1",
                Angle.fromDegrees(323.982),
                1000 + RADIUS_OF_JUPITER,
                "StandardSatellite"),
            controller.getInfo("Satellite1")
        );
    }

    @Test
    public void testTeleportingMovement2() {

        BlackoutController controller = new BlackoutController();

        controller.createSatellite("Satellite1", "TeleportingSatellite", 20000 + RADIUS_OF_JUPITER,
                        Angle.fromDegrees(100));

        controller.simulate();
        assertEquals(
            new EntityInfoResponse("Satellite1",
                Angle.fromDegrees(100.64),
                20000 + RADIUS_OF_JUPITER,
                "TeleportingSatellite"),
            controller.getInfo("Satellite1")
        );
        Angle clockwiseOnFirstMovement = controller.getInfo("Satellite1").getPosition();

        controller.simulate(60);
        assertEquals(
            new EntityInfoResponse("Satellite1",
                Angle.fromDegrees(138.87),
                20000 + RADIUS_OF_JUPITER,
                "TeleportingSatellite"),
            controller.getInfo("Satellite1")
        );
        Angle clockwiseOnSecondMovement = controller.getInfo("Satellite1").getPosition();
        assertTrue(clockwiseOnSecondMovement.compareTo(clockwiseOnFirstMovement) == 1);

        //test teleport
        controller.createSatellite("Satellite2", "TeleportingSatellite", 100 + RADIUS_OF_JUPITER,
                        Angle.fromDegrees(175));

        controller.simulate(6);
        assertEquals(
            new EntityInfoResponse("Satellite2",
                Angle.fromDegrees(179.9104),
                100 + RADIUS_OF_JUPITER,
                "TeleportingSatellite"),
            controller.getInfo("Satellite2")
        );
        controller.simulate(1);
        assertEquals(
            new EntityInfoResponse("Satellite2",
                Angle.fromDegrees(0),
                100 + RADIUS_OF_JUPITER,
                "TeleportingSatellite"),
            controller.getInfo("Satellite2")
        );
        controller.createSatellite("Satellite2", "TeleportingSatellite", 100 + RADIUS_OF_JUPITER,
                        Angle.fromDegrees(175));

        controller.simulate(6);
        assertEquals(
            new EntityInfoResponse("Satellite2",
                Angle.fromDegrees(179.9104),
                100 + RADIUS_OF_JUPITER,
                "TeleportingSatellite"),
            controller.getInfo("Satellite2")
        );
        controller.simulate(1);
        assertEquals(
            new EntityInfoResponse("Satellite2",
                Angle.fromDegrees(0),
                100 + RADIUS_OF_JUPITER,
                "TeleportingSatellite"),
            controller.getInfo("Satellite2")
        );

        //test change movedirection
        controller.simulate(1);
        assertEquals(
            new EntityInfoResponse("Satellite2",
                Angle.fromDegrees(359.18),
                100 + RADIUS_OF_JUPITER,
                "TeleportingSatellite"),
            controller.getInfo("Satellite2")
        );
        //test teleport and change move direction again
        controller.simulate(219);
        assertEquals(
            new EntityInfoResponse("Satellite2",
                Angle.fromDegrees(0),
                100 + RADIUS_OF_JUPITER,
                "TeleportingSatellite"),
            controller.getInfo("Satellite2")
        );
        controller.simulate();
        assertEquals(
            new EntityInfoResponse("Satellite2",
                Angle.fromDegrees(0.82),
                100 + RADIUS_OF_JUPITER,
                "TeleportingSatellite"),
            controller.getInfo("Satellite2")
        );
    }

    @Test
    public void testRelayMovement2() {
        BlackoutController controller = new BlackoutController();

        controller.createSatellite("Satellite1", "RelaySatellite", 1000 + RADIUS_OF_JUPITER,
                                Angle.fromDegrees(150));

        assertEquals(
                        new EntityInfoResponse("Satellite1", Angle.fromDegrees(150), 1000 + RADIUS_OF_JUPITER,
                                        "RelaySatellite"),
                        controller.getInfo("Satellite1"));

        controller.simulate();
        assertEquals(new EntityInfoResponse("Satellite1", Angle.fromDegrees(148.79), 1000 + RADIUS_OF_JUPITER,
                        "RelaySatellite"), controller.getInfo("Satellite1"));


        controller.simulate(7);
        assertEquals(new EntityInfoResponse("Satellite1", Angle.fromDegrees(140.30), 1000 + RADIUS_OF_JUPITER,
                        "RelaySatellite"), controller.getInfo("Satellite1"));
        // edge case
        controller.simulate();
        assertEquals(new EntityInfoResponse("Satellite1", Angle.fromDegrees(139.09), 1000 + RADIUS_OF_JUPITER,
                        "RelaySatellite"), controller.getInfo("Satellite1"));
        // coming back
        controller.simulate();
        assertEquals(new EntityInfoResponse("Satellite1", Angle.fromDegrees(140.30), 1000 + RADIUS_OF_JUPITER,
                        "RelaySatellite"), controller.getInfo("Satellite1"));

        //change move direction
        controller.simulate(10);
        assertEquals(new EntityInfoResponse("Satellite1", Angle.fromDegrees(152.42), 1000 + RADIUS_OF_JUPITER,
                        "RelaySatellite"), controller.getInfo("Satellite1"));
        controller.simulate(31);
        assertEquals(new EntityInfoResponse("Satellite1", Angle.fromDegrees(189.99), 1000 + RADIUS_OF_JUPITER,
                        "RelaySatellite"), controller.getInfo("Satellite1"));
        controller.simulate(1);
        assertEquals(new EntityInfoResponse("Satellite1", Angle.fromDegrees(191.21), 1000 + RADIUS_OF_JUPITER,
                        "RelaySatellite"), controller.getInfo("Satellite1"));

        //come back and change move dirction again
        controller.simulate(1);
        assertEquals(new EntityInfoResponse("Satellite1", Angle.fromDegrees(189.99), 1000 + RADIUS_OF_JUPITER,
                        "RelaySatellite"), controller.getInfo("Satellite1"));
        controller.simulate(10);
        assertEquals(new EntityInfoResponse("Satellite1", Angle.fromDegrees(177.88), 1000 + RADIUS_OF_JUPITER,
                        "RelaySatellite"), controller.getInfo("Satellite1"));
    }


    @Test
    public void testEntitiesInRange2() {
        BlackoutController controller = new BlackoutController();
        //test two standardSatellite
        controller.createSatellite("Satellite1", "StandardSatellite",
        20000 + RADIUS_OF_JUPITER, Angle.fromDegrees(158));
        controller.createSatellite("Satellite2", "StandardSatellite",
        20000 + RADIUS_OF_JUPITER, Angle.fromDegrees(179));
        assertListAreEqualIgnoringOrder(
            Arrays.asList("Satellite2"),
            controller.communicableEntitiesInRange("Satellite1"));
        assertListAreEqualIgnoringOrder(
            Arrays.asList("Satellite1"),
            controller.communicableEntitiesInRange("Satellite2"));
        // test standardSatellite and (LaptopDevice,HandheldDevice)
        controller.createDevice("DeviceA", "HandheldDevice", Angle.fromDegrees(173));
        controller.createDevice("DeviceB", "LaptopDevice", Angle.fromDegrees(177));
        assertListAreEqualIgnoringOrder(
            Arrays.asList("Satellite2", "DeviceA", "DeviceB"),
            controller.communicableEntitiesInRange("Satellite1"));
        assertListAreEqualIgnoringOrder(
            Arrays.asList("Satellite1", "DeviceA", "DeviceB"),
            controller.communicableEntitiesInRange("Satellite2"));
        assertListAreEqualIgnoringOrder(
            Arrays.asList("Satellite2", "Satellite1"),
            controller.communicableEntitiesInRange("DeviceA"));
        assertListAreEqualIgnoringOrder(
            Arrays.asList("Satellite2", "Satellite1"),
            controller.communicableEntitiesInRange("DeviceB"));

        // test standardSatellite and DesktopDevice
        controller.createDevice("DeviceC", "DesktopDevice", Angle.fromDegrees(175));
        assertListAreEqualIgnoringOrder(
            Arrays.asList("Satellite2", "DeviceA", "DeviceB"),
            controller.communicableEntitiesInRange("Satellite1"));
        assertListAreEqualIgnoringOrder(
            Arrays.asList("Satellite1", "DeviceA", "DeviceB"),
            controller.communicableEntitiesInRange("Satellite2"));

        // test over distance
        controller.createSatellite("Satellite3", "StandardSatellite",
        200000 + RADIUS_OF_JUPITER, Angle.fromDegrees(158));
        assertListAreEqualIgnoringOrder(
            Arrays.asList("Satellite1", "DeviceA", "DeviceB"),
            controller.communicableEntitiesInRange("Satellite2"));
        assertListAreEqualIgnoringOrder(
            Arrays.asList("Satellite1", "DeviceA", "DeviceB"),
            controller.communicableEntitiesInRange("Satellite2"));
        assertListAreEqualIgnoringOrder(
            Arrays.asList(),
            controller.communicableEntitiesInRange("Satellite3"));

        // test invisible
        controller.createSatellite("Satellite4", "TeleportingSatellite",
        20000 + RADIUS_OF_JUPITER, Angle.fromDegrees(15));
        assertListAreEqualIgnoringOrder(
            Arrays.asList(),
            controller.communicableEntitiesInRange("Satellite4"));
        BlackoutController controller2 = new BlackoutController();

        // test RelaySatellite helps communication
        controller2.createSatellite("Satellite5", "TeleportingSatellite",
        20000 + RADIUS_OF_JUPITER, Angle.fromDegrees(70));
        controller2.createSatellite("Satellite6", "StandardSatellite",
        60000 + RADIUS_OF_JUPITER, Angle.fromDegrees(320));
        assertListAreEqualIgnoringOrder(
            Arrays.asList(),
            controller2.communicableEntitiesInRange("Satellite5"));
        assertListAreEqualIgnoringOrder(
            Arrays.asList(),
            controller2.communicableEntitiesInRange("Satellite6"));
        controller2.createSatellite("Satellite7", "RelaySatellite",
        20000 + RADIUS_OF_JUPITER, Angle.fromDegrees(354));
        assertListAreEqualIgnoringOrder(
            Arrays.asList("Satellite7", "Satellite6"),
            controller2.communicableEntitiesInRange("Satellite5"));
        assertListAreEqualIgnoringOrder(
            Arrays.asList("Satellite7", "Satellite5"),
            controller2.communicableEntitiesInRange("Satellite6"));
        assertListAreEqualIgnoringOrder(
            Arrays.asList("Satellite5", "Satellite6"),
            controller2.communicableEntitiesInRange("Satellite7"));

        // test several RelaySatellites help communication
        BlackoutController controller3 = new BlackoutController();
        controller3.createSatellite("Satellite8", "StandardSatellite",
        45000 + RADIUS_OF_JUPITER, Angle.fromDegrees(47));
        controller3.createSatellite("Satellite9", "StandardSatellite",
        30000 + RADIUS_OF_JUPITER, Angle.fromDegrees(301));
        assertListAreEqualIgnoringOrder(
            Arrays.asList(),
            controller3.communicableEntitiesInRange("Satellite8"));
        controller3.createSatellite("Satellite10", "RelaySatellite",
            45000 + RADIUS_OF_JUPITER, Angle.fromDegrees(21));
        controller3.createSatellite("Satellite11", "RelaySatellite",
            30000 + RADIUS_OF_JUPITER, Angle.fromDegrees(344));
        assertListAreEqualIgnoringOrder(
            Arrays.asList("Satellite9", "Satellite10", "Satellite11"),
            controller3.communicableEntitiesInRange("Satellite8"));
        assertListAreEqualIgnoringOrder(
            Arrays.asList("Satellite8", "Satellite10", "Satellite11"),
            controller3.communicableEntitiesInRange("Satellite9"));

        //test DesktopDevice and StandardSatellite can not communicate each other , even with the RelaySatellites' help
        controller3.createDevice("DeviceD", "DesktopDevice", Angle.fromDegrees(58));
        controller3.createDevice("DeviceE", "DesktopDevice", Angle.fromDegrees(306));
        assertListAreEqualIgnoringOrder(
            Arrays.asList("Satellite10", "Satellite11"),
            controller3.communicableEntitiesInRange("DeviceD"));
        assertListAreEqualIgnoringOrder(
            Arrays.asList("Satellite10", "Satellite11"),
            controller3.communicableEntitiesInRange("DeviceE"));
        assertListAreEqualIgnoringOrder(
            Arrays.asList("Satellite9", "Satellite10", "Satellite11"),
            controller3.communicableEntitiesInRange("Satellite8"));
        assertListAreEqualIgnoringOrder(
            Arrays.asList("Satellite8", "Satellite10", "Satellite11"),
            controller3.communicableEntitiesInRange("Satellite9"));

        //test devices can not communicate each other , even with the RelaySatellites' help
        BlackoutController controller4 = new BlackoutController();
        controller4.createDevice("DeviceF", "DesktopDevice", Angle.fromDegrees(100));
        controller4.createDevice("DeviceG", "DesktopDevice", Angle.fromDegrees(120));
        controller4.createSatellite("Satellite12", "RelaySatellite",
        10000 + RADIUS_OF_JUPITER, Angle.fromDegrees(110));
        assertListAreEqualIgnoringOrder(
            Arrays.asList("Satellite12"),
            controller4.communicableEntitiesInRange("DeviceF"));
        assertListAreEqualIgnoringOrder(
            Arrays.asList("Satellite12"),
            controller4.communicableEntitiesInRange("DeviceG"));

        //test RelaySatellites to RelaySatellites to....
        BlackoutController controller5 = new BlackoutController();
        controller5.createSatellite("TeleportingSatellite1", "TeleportingSatellite",
        36000 + RADIUS_OF_JUPITER, Angle.fromDegrees(158));
        controller5.createSatellite("TeleportingSatellite2", "TeleportingSatellite",
        32000 + RADIUS_OF_JUPITER, Angle.fromDegrees(357));
        assertListAreEqualIgnoringOrder(
            Arrays.asList(),
            controller5.communicableEntitiesInRange("TeleportingSatellite1"));
        controller5.createSatellite("RelaySatellite1", "RelaySatellite",
        20000 + RADIUS_OF_JUPITER, Angle.fromDegrees(124));
        controller5.createSatellite("RelaySatellite2", "RelaySatellite",
        15000 + RADIUS_OF_JUPITER, Angle.fromDegrees(84));
        controller5.createSatellite("RelaySatellite3", "RelaySatellite",
        22000 + RADIUS_OF_JUPITER, Angle.fromDegrees(38));
        assertListAreEqualIgnoringOrder(
            Arrays.asList("TeleportingSatellite2", "RelaySatellite1", "RelaySatellite2", "RelaySatellite3"),
            controller5.communicableEntitiesInRange("TeleportingSatellite1"));
    }


    @Test
    public void testSendFiles() {
    BlackoutController controller = new BlackoutController();

    controller.createSatellite("Satellite1", "StandardSatellite", 11000 + RADIUS_OF_JUPITER, Angle.fromDegrees(3));
    controller.createSatellite("Satellite2",
        "TeleportingSatellite", 12000 + RADIUS_OF_JUPITER, Angle.fromDegrees(39));
    controller.createDevice("DeviceC", "LaptopDevice", Angle.fromDegrees(28));



    //test TeleportingSatellite receive files
    String msg = "Heyheyheyhehyeyhehyeheyhhehyehehehyehyheheeyheyhe";
    //test the progress of the message half way through
    controller.addFileToDevice("DeviceC", "FileAlpha", msg);
    assertDoesNotThrow(() -> controller.sendFile("FileAlpha", "DeviceC", "Satellite2"));
    controller.simulate();
    Map<String, FileInfoResponse> expected = new HashMap<>();
    expected.put(
            "FileAlpha",
            new FileInfoResponse("FileAlpha", "Heyheyheyhehyey",
            msg.length(), false)
        );
    assertEquals(
        new EntityInfoResponse("Satellite2", Angle.fromDegrees(39.70),
        12000 + RADIUS_OF_JUPITER, "TeleportingSatellite", expected),
        controller.getInfo("Satellite2")
    );

    controller.simulate(3);
    Map<String, FileInfoResponse> expected1 = new HashMap<>();
    expected1.put(
            "FileAlpha",
            new FileInfoResponse("FileAlpha", msg,
            msg.length(), true)
        );
    assertEquals(
        new EntityInfoResponse("Satellite2", Angle.fromDegrees(41.80),
        12000 + RADIUS_OF_JUPITER, "TeleportingSatellite", expected1),
        controller.getInfo("Satellite2")
    );

    //Device/s get out of range of satellite
    assertDoesNotThrow(() -> controller.sendFile("FileAlpha", "DeviceC", "Satellite1"));
    controller.simulate(20);
    Map<String, FileInfoResponse> expected2 = new HashMap<>();
    assertEquals(
        new EntityInfoResponse("Satellite1", Angle.fromDegrees(320.52),
        11000 + RADIUS_OF_JUPITER, "StandardSatellite", expected2),
        controller.getInfo("Satellite1")
    );

    }

    @Test
    public void testSendFiles2() {
    BlackoutController controller = new BlackoutController();
    controller.createSatellite("Satellite",
        "TeleportingSatellite", 20000 + RADIUS_OF_JUPITER, Angle.fromDegrees(178));
    controller.createDevice("DeviceC", "LaptopDevice", Angle.fromDegrees(175));

    String msg = "ttteteteteeteteteteteteteteteteteteteteeteteteteteteteteetetetetetetetetetetetetetetetetet";
    controller.addFileToDevice("DeviceC", "File", msg);
    assertDoesNotThrow(() -> controller.sendFile("File", "DeviceC", "Satellite"));
    controller.simulate(4);
    Map<String, FileInfoResponse> expected = new HashMap<>();
    //test TeleportingSatellite teleports when device send file to TeleportingSatellite
    assertEquals(
        new EntityInfoResponse("Satellite", Angle.fromDegrees(0),
        20000 + RADIUS_OF_JUPITER, "TeleportingSatellite", expected),
        controller.getInfo("Satellite")
    );
    Map<String, FileInfoResponse> expected1 = new HashMap<>();
     expected1.put(
            "File",
            new FileInfoResponse("File", "eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee",
            "eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee".length(), true)
        );
    assertEquals(
        new EntityInfoResponse("DeviceC", Angle.fromDegrees(175),
        RADIUS_OF_JUPITER, "LaptopDevice", expected1),
        controller.getInfo("DeviceC"));

    //test TeleportingSatellite teleports when TeleportingSatellite send file to entities
    controller.addFileToDevice("DeviceC", "File2", msg);
    controller.createSatellite("Satellite2",
    "TeleportingSatellite", 20000 + RADIUS_OF_JUPITER, Angle.fromDegrees(170));
    assertDoesNotThrow(() -> controller.sendFile("File2", "DeviceC", "Satellite2"));
    controller.simulate(14);
    controller.createDevice("DeviceD", "LaptopDevice", Angle.fromDegrees(176));
    assertDoesNotThrow(() -> controller.sendFile("File2", "Satellite2", "DeviceD"));
    controller.simulate(2);
    Map<String, FileInfoResponse> expected2 = new HashMap<>();
     expected2.put(
            "File2",
            new FileInfoResponse("File2", "ttteteteteeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee",
            "ttteteteteeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee".length(), true)
        );
    assertEquals(
        new EntityInfoResponse("DeviceD", Angle.fromDegrees(176),
        RADIUS_OF_JUPITER, "LaptopDevice", expected2),
        controller.getInfo("DeviceD"));
    }

    @Test
    public void testSendFiles3() {
        //test send or receive several files
        BlackoutController controller = new BlackoutController();
        controller.createSatellite("Satellite",
            "TeleportingSatellite", 20000 + RADIUS_OF_JUPITER, Angle.fromDegrees(170));
        controller.createDevice("DeviceC", "LaptopDevice", Angle.fromDegrees(175));
        String msg = "tttetet";
        String msg1 = "ttte";
        String msg2 = "t";
        String msg3 = "tttetete";
        controller.addFileToDevice("DeviceC", "File", msg);
        controller.addFileToDevice("DeviceC", "File1", msg1);
        controller.addFileToDevice("DeviceC", "File2", msg2);
        controller.addFileToDevice("DeviceC", "File3", msg3);
        assertDoesNotThrow(() -> controller.sendFile("File", "DeviceC", "Satellite"));
        assertDoesNotThrow(() -> controller.sendFile("File1", "DeviceC", "Satellite"));
        assertDoesNotThrow(() -> controller.sendFile("File2", "DeviceC", "Satellite"));
        assertDoesNotThrow(() -> controller.sendFile("File3", "DeviceC", "Satellite"));
        controller.simulate();
        Map<String, FileInfoResponse> expected = new HashMap<>();
        expected.put(
            "File3",
            new FileInfoResponse("File3", "ttt",
            msg3.length(), false)
        );
        expected.put(
            "File",
            new FileInfoResponse("File", "ttt",
            msg.length(), false)
        );
        expected.put(
            "File2",
            new FileInfoResponse("File2", "t",
            msg2.length(), true)
        );
        expected.put(
            "File1",
            new FileInfoResponse("File1", "ttt",
            msg1.length(), false)
        );
        assertEquals(
            new EntityInfoResponse("Satellite", Angle.fromDegrees(170.64),
            20000 + RADIUS_OF_JUPITER, "TeleportingSatellite", expected),
            controller.getInfo("Satellite"));

        controller.simulate();
        Map<String, FileInfoResponse> expected1 = new HashMap<>();
        expected1.put(
            "File3",
            new FileInfoResponse("File3", "tttetete",
            msg3.length(), true)
        );
        expected1.put(
            "File",
            new FileInfoResponse("File", "tttetet",
            msg.length(), true)
        );
        expected1.put(
            "File2",
            new FileInfoResponse("File2", "t",
            msg2.length(), true)
        );
        expected1.put(
            "File1",
            new FileInfoResponse("File1", "ttte",
            msg1.length(), true)
        );
        assertEquals(
            new EntityInfoResponse("Satellite", Angle.fromDegrees(171.28),
            20000 + RADIUS_OF_JUPITER, "TeleportingSatellite", expected1),
            controller.getInfo("Satellite"));
        }

    @Test
    public void testSomeExceptions() {
        BlackoutController controller = new BlackoutController();

        controller.createSatellite("Satellite1", "StandardSatellite", 5000 + RADIUS_OF_JUPITER, Angle.fromDegrees(320));
        controller.createSatellite("Satellite2", "RelaySatellite", 10000 + RADIUS_OF_JUPITER, Angle.fromDegrees(300));
        controller.createSatellite("Satellite3",
        "TeleportingSatellite", 5000 + RADIUS_OF_JUPITER, Angle.fromDegrees(310));
        controller.createDevice("DeviceB", "LaptopDevice", Angle.fromDegrees(310));
        controller.createDevice("DeviceC", "HandheldDevice", Angle.fromDegrees(320));
        String msg = "Hello";
        controller.addFileToDevice("DeviceB", "File", msg);
        controller.addFileToDevice("DeviceB", "File2", msg);
        //test RelaySatellite can not receive and send file
        assertThrows(
            FileTransferException.VirtualFileNoBandwidthException.class,
            () -> controller.sendFile("File", "DeviceB", "Satellite2"));

        //test filenotFound
        assertDoesNotThrow(() -> controller.sendFile("File2", "DeviceB", "Satellite1"));
        assertThrows(
            FileTransferException.VirtualFileNotFoundException.class,
            () -> controller.sendFile("File", "Satellite1", "DeviceC"));

        //test FileAlreadyExists
        assertDoesNotThrow(() -> controller.sendFile("File2", "DeviceB", "Satellite3"));
        assertThrows(
            FileTransferException.VirtualFileAlreadyExistsException.class,
            () -> controller.sendFile("File2", "DeviceB", "Satellite3"));

        //test no band
        //test receive band
        assertThrows(
            FileTransferException.VirtualFileNoBandwidthException.class,
            () -> controller.sendFile("File", "DeviceB", "Satellite1"));

        controller.simulate(5);
        Map<String, FileInfoResponse> expected = new HashMap<>();
        expected.put(
            "File2",
            new FileInfoResponse("File2", msg,
            msg.length(), true)
        );
        assertEquals(
            new EntityInfoResponse("Satellite1", Angle.fromDegrees(310.44),
            5000 + RADIUS_OF_JUPITER, "StandardSatellite", expected),
            controller.getInfo("Satellite1")
        );
        assertDoesNotThrow(() -> controller.sendFile("File", "DeviceB", "Satellite1"));
        controller.simulate(5);
        assertDoesNotThrow(() -> controller.sendFile("File", "Satellite1", "Satellite3"));

        //test send band
        assertThrows(
            FileTransferException.VirtualFileNoBandwidthException.class,
            () -> controller.sendFile("File2", "Satellite1", "Satellite3"));


        //test no storage
        String msg2 = "HelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHello";
        String msg3 = "HelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHello";
        String msg4 = "HelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHelloHello";
        controller.addFileToDevice("DeviceC", "File5", msg2);
        controller.addFileToDevice("DeviceC", "File6", msg3);
        controller.addFileToDevice("DeviceC", "File7", msg4);
        assertDoesNotThrow(() -> controller.sendFile("File5", "DeviceC", "Satellite3"));
        assertDoesNotThrow(() -> controller.sendFile("File6", "DeviceC", "Satellite3"));
        assertThrows(
            FileTransferException.VirtualFileNoStorageSpaceException.class,
            () -> controller.sendFile("File7", "DeviceC", "Satellite3"));
    }
}
