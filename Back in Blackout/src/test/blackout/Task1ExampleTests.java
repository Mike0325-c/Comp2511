package blackout;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import unsw.blackout.BlackoutController;
import unsw.blackout.FileTransferException;
import unsw.response.models.EntityInfoResponse;
import unsw.response.models.FileInfoResponse;
import unsw.utils.Angle;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static blackout.TestHelpers.assertListAreEqualIgnoringOrder;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static unsw.utils.MathsHelper.RADIUS_OF_JUPITER;

@TestInstance(value = Lifecycle.PER_CLASS)
public class Task1ExampleTests {
    @Test
    public void testExample() {
        // Task 1
        // Example from the specification
        BlackoutController controller = new BlackoutController();

        // Creates 1 satellite and 3 devices
        // 2 devices are in view of the satellite
        // 1 device is out of view of the satellite
        controller.createSatellite("Satellite1", "StandardSatellite", 100 + RADIUS_OF_JUPITER, Angle.fromDegrees(340));
        controller.createDevice("DeviceA", "HandheldDevice", Angle.fromDegrees(30));
        controller.createDevice("DeviceB", "LaptopDevice", Angle.fromDegrees(180));
        controller.createDevice("DeviceC", "DesktopDevice", Angle.fromDegrees(330));

        assertListAreEqualIgnoringOrder(Arrays.asList("Satellite1"), controller.listSatelliteIds());
        assertListAreEqualIgnoringOrder(Arrays.asList("DeviceA", "DeviceB", "DeviceC"), controller.listDeviceIds());

        assertEquals(new EntityInfoResponse("Satellite1", Angle.fromDegrees(340),
         100 + RADIUS_OF_JUPITER, "StandardSatellite"), controller.getInfo("Satellite1"));

        assertEquals(new EntityInfoResponse("DeviceA", Angle.fromDegrees(30),
         RADIUS_OF_JUPITER, "HandheldDevice"), controller.getInfo("DeviceA"));
        assertEquals(new EntityInfoResponse("DeviceB", Angle.fromDegrees(180),
         RADIUS_OF_JUPITER, "LaptopDevice"), controller.getInfo("DeviceB"));
        assertEquals(new EntityInfoResponse("DeviceC", Angle.fromDegrees(330),
         RADIUS_OF_JUPITER, "DesktopDevice"), controller.getInfo("DeviceC"));
    }

    @Test
    public void testDelete() {
        // Task 1
        BlackoutController controller = new BlackoutController();

        // Creates 1 satellite and 3 devices and deletes them
        controller.createSatellite("Satellite1", "StandardSatellite", 100 + RADIUS_OF_JUPITER, Angle.fromDegrees(340));
        controller.createDevice("DeviceA", "HandheldDevice", Angle.fromDegrees(30));
        controller.createDevice("DeviceB", "LaptopDevice", Angle.fromDegrees(180));
        controller.createDevice("DeviceC", "DesktopDevice", Angle.fromDegrees(330));

        assertListAreEqualIgnoringOrder(Arrays.asList("Satellite1"), controller.listSatelliteIds());
        assertListAreEqualIgnoringOrder(Arrays.asList("DeviceA", "DeviceB", "DeviceC"), controller.listDeviceIds());

        controller.removeDevice("DeviceA");
        controller.removeDevice("DeviceB");
        controller.removeDevice("DeviceC");
        controller.removeSatellite("Satellite1");
    }

    @Test
    public void basicFileSupport() {
        // Task 1
        BlackoutController controller = new BlackoutController();

        // Creates 1 device and add some files to it
        controller.createDevice("DeviceC", "DesktopDevice", Angle.fromDegrees(330));
        assertListAreEqualIgnoringOrder(Arrays.asList("DeviceC"), controller.listDeviceIds());
        assertEquals(new EntityInfoResponse("DeviceC",
         Angle.fromDegrees(330), RADIUS_OF_JUPITER, "DesktopDevice"), controller.getInfo("DeviceC"));

        controller.addFileToDevice("DeviceC", "Hello World", "My first file!");
        Map<String, FileInfoResponse> expected = new HashMap<>();
        expected.put("Hello World", new FileInfoResponse("Hello World",
         "My first file!", "My first file!".length(), true));
        assertEquals(new EntityInfoResponse("DeviceC", Angle.fromDegrees(330),
         RADIUS_OF_JUPITER, "DesktopDevice", expected), controller.getInfo("DeviceC"));
    }


//addtional tests
    @Test
    public void testtask1() throws FileTransferException {
        BlackoutController controller = new BlackoutController();

    // Creates 1 satellite and 2 devices
    // Gets a device to send a file to a satellites and gets another device to download it.
    // StandardSatellites are slow and transfer 1 byte per minute.
    controller.createSatellite("Satellite1", "StandardSatellite", 10000 + RADIUS_OF_JUPITER, Angle.fromDegrees(320));
    controller.createDevice("DeviceB", "LaptopDevice", Angle.fromDegrees(310));
    controller.createDevice("DeviceC", "HandheldDevice", Angle.fromDegrees(320));

    String msg = "Hi 42";
    controller.addFileToDevice("DeviceC", "FileAlpha", msg);
    controller.sendFile("FileAlpha", "DeviceC", "Satellite1");
    assertEquals(new FileInfoResponse("FileAlpha", "", msg.length(),
     false), controller.getInfo("Satellite1").getFiles().get("FileAlpha"));

    controller.simulate(msg.length());
    assertEquals(new FileInfoResponse("FileAlpha", msg, msg.length(), true),
     controller.getInfo("Satellite1").getFiles().get("FileAlpha"));

    controller.sendFile("FileAlpha", "Satellite1", "DeviceB");
    assertEquals(new FileInfoResponse("FileAlpha", "", msg.length(), false),
     controller.getInfo("DeviceB").getFiles().get("FileAlpha"));

    controller.simulate(msg.length());
    assertEquals(new FileInfoResponse("FileAlpha", msg, msg.length(), true),
     controller.getInfo("DeviceB").getFiles().get("FileAlpha"));
    }

    @Test
    public void testCreateOrRemoveentity() {

        BlackoutController controller = new BlackoutController();
        //create
        controller.createSatellite("Satellite1", "StandardSatellite", 5000 + RADIUS_OF_JUPITER, Angle.fromDegrees(340));
        controller.createSatellite("Satellite2", "StandardSatellite", 6000 + RADIUS_OF_JUPITER, Angle.fromDegrees(320));
        controller.createSatellite("Satellite3", "TeleportingSatellite",
         11100 + RADIUS_OF_JUPITER, Angle.fromDegrees(240));
         controller.createSatellite("Satellite4", "TeleportingSatellite",
         22100 + RADIUS_OF_JUPITER, Angle.fromDegrees(140));
         controller.createSatellite("Satellite5", "RelaySatellite", 12300 + RADIUS_OF_JUPITER, Angle.fromDegrees(34));
         controller.createSatellite("Satellite6", "RelaySatellite", 10510 + RADIUS_OF_JUPITER, Angle.fromDegrees(120));

         assertListAreEqualIgnoringOrder(Arrays.asList("Satellite1", "Satellite2", "Satellite3",
        "Satellite4", "Satellite5", "Satellite6"), controller.listSatelliteIds());

        controller.createDevice("DeviceA", "HandheldDevice", Angle.fromDegrees(30));
        controller.createDevice("DeviceB", "LaptopDevice", Angle.fromDegrees(180));
        controller.createDevice("DeviceC", "DesktopDevice", Angle.fromDegrees(330));

        assertListAreEqualIgnoringOrder(Arrays.asList("DeviceA", "DeviceB", "DeviceC"), controller.listDeviceIds());

        //delete
        controller.removeDevice("DeviceA");
        assertListAreEqualIgnoringOrder(Arrays.asList("DeviceB", "DeviceC"), controller.listDeviceIds());
        controller.removeDevice("DeviceB");
        assertListAreEqualIgnoringOrder(Arrays.asList("DeviceC"), controller.listDeviceIds());
        controller.removeDevice("DeviceC");
        assertListAreEqualIgnoringOrder(Arrays.asList(), controller.listDeviceIds());

        controller.removeSatellite("Satellite1");
        assertListAreEqualIgnoringOrder(Arrays.asList("Satellite2", "Satellite3",
        "Satellite4", "Satellite5", "Satellite6"), controller.listSatelliteIds());
        controller.removeSatellite("Satellite2");
        assertListAreEqualIgnoringOrder(Arrays.asList("Satellite3",
        "Satellite4", "Satellite5", "Satellite6"), controller.listSatelliteIds());
        controller.removeSatellite("Satellite3");
        assertListAreEqualIgnoringOrder(Arrays.asList("Satellite4",
         "Satellite5", "Satellite6"), controller.listSatelliteIds());
        controller.removeSatellite("Satellite4");
        assertListAreEqualIgnoringOrder(Arrays.asList("Satellite5", "Satellite6"), controller.listSatelliteIds());
        controller.removeSatellite("Satellite5");
        assertListAreEqualIgnoringOrder(Arrays.asList("Satellite6"), controller.listSatelliteIds());
        controller.removeSatellite("Satellite6");
        assertListAreEqualIgnoringOrder(Arrays.asList(), controller.listSatelliteIds());
    }

    @Test
    public void testaddFileOrGetInfo() {
        BlackoutController controller = new BlackoutController();

        controller.createDevice("DeviceA", "HandheldDevice", Angle.fromDegrees(33));
        assertEquals(
            new EntityInfoResponse("DeviceA", Angle.fromDegrees(33), RADIUS_OF_JUPITER, "HandheldDevice"),
            controller.getInfo("DeviceA")
        );

        controller.createDevice("DeviceB", "LaptopDevice", Angle.fromDegrees(250));
        assertEquals(
            new EntityInfoResponse("DeviceB", Angle.fromDegrees(250), RADIUS_OF_JUPITER, "LaptopDevice"),
            controller.getInfo("DeviceB")
        );

        controller.createDevice("DeviceC", "DesktopDevice", Angle.fromDegrees(130));
        assertEquals(
            new EntityInfoResponse("DeviceC", Angle.fromDegrees(130), RADIUS_OF_JUPITER, "DesktopDevice"),
            controller.getInfo("DeviceC")
        );

        //test device information
        //add files to deviceA
        controller.addFileToDevice("DeviceA", "Hello COMP2511", "I love COMP2511");
        Map<String, FileInfoResponse> expected = new HashMap<>();
        expected.put(
            "Hello COMP2511",
            new FileInfoResponse("Hello COMP2511", "I love COMP2511", "I love COMP2511".length(), true)
        );
        assertEquals(
            new EntityInfoResponse("DeviceA", Angle.fromDegrees(33), RADIUS_OF_JUPITER, "HandheldDevice", expected),
            controller.getInfo("DeviceA")
        );

        //add files to deviceB
        controller.addFileToDevice("DeviceB", "HelloJJYJJYJJYJJYJJYJJY", "JJYJJYJJYJJYJJYJJY");
        Map<String, FileInfoResponse> expected2 = new HashMap<>();
        expected2.put(
            "HelloJJYJJYJJYJJYJJYJJY",
            new FileInfoResponse("HelloJJYJJYJJYJJYJJYJJY", "JJYJJYJJYJJYJJYJJY", "JJYJJYJJYJJYJJYJJY".length(), true)
        );
        assertEquals(
            new EntityInfoResponse("DeviceB", Angle.fromDegrees(250), RADIUS_OF_JUPITER, "LaptopDevice", expected2),
            controller.getInfo("DeviceB")
        );

        //add files to deviceC
        controller.addFileToDevice("DeviceC", "unsw", "Welcome to the UNSW Moodle platform.");
        Map<String, FileInfoResponse> expected3 = new HashMap<>();
        expected3.put(
            "unsw",
            new FileInfoResponse("unsw", "Welcome to the UNSW Moodle platform.",
             "Welcome to the UNSW Moodle platform.".length(), true)
        );
        assertEquals(
            new EntityInfoResponse("DeviceC", Angle.fromDegrees(130), RADIUS_OF_JUPITER, "DesktopDevice", expected3),
            controller.getInfo("DeviceC")
        );

        //test satellite information
        controller.createSatellite("Satellite1", "StandardSatellite", 5000 + RADIUS_OF_JUPITER, Angle.fromDegrees(340));
        controller.createSatellite("Satellite2", "TeleportingSatellite",
        11100 + RADIUS_OF_JUPITER, Angle.fromDegrees(240));
        controller.createSatellite("Satellite3", "RelaySatellite", 12300 + RADIUS_OF_JUPITER, Angle.fromDegrees(34));
        assertEquals(
            new EntityInfoResponse("Satellite1", Angle.fromDegrees(340), 5000 + RADIUS_OF_JUPITER, "StandardSatellite"),
            controller.getInfo("Satellite1")
        );
        assertEquals(
            new EntityInfoResponse("Satellite2", Angle.fromDegrees(240),
            11100 + RADIUS_OF_JUPITER, "TeleportingSatellite"),
            controller.getInfo("Satellite2")
        );
        assertEquals(
            new EntityInfoResponse("Satellite3", Angle.fromDegrees(34), 12300 + RADIUS_OF_JUPITER, "RelaySatellite"),
            controller.getInfo("Satellite3")
        );
    }
}
