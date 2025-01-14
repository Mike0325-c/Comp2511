package unsw.blackout;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import unsw.response.models.EntityInfoResponse;
import unsw.response.models.FileInfoResponse;
import unsw.utils.Angle;
import unsw.utils.MathsHelper;

import static unsw.utils.MathsHelper.RADIUS_OF_JUPITER;

public class BlackoutController {
    private Map<String, AllDevice> devices = new HashMap<String, AllDevice>();
    private Map<String, AllSatellite> satellites = new HashMap<String, AllSatellite>();
    private List<TransferingFiles> transfer = new ArrayList<TransferingFiles>();
    private boolean add = true;



    public void createDevice(String deviceId, String type, Angle position) {
        if (type.equals("HandheldDevice")) {
            HandheldDevice newHandheld = new HandheldDevice(deviceId, type, position);
            devices.put(deviceId, newHandheld);
        } else if (type.equals("LaptopDevice")) {
            LaptopDevice newLaptop = new LaptopDevice(deviceId, type, position);
            devices.put(deviceId, newLaptop);
        } else if (type.equals("DesktopDevice")) {
            DesktopDevice newDesktop = new DesktopDevice(deviceId, type, position);
            devices.put(deviceId, newDesktop);
        }
    }

    public void removeDevice(String deviceId) {
            devices.remove(deviceId);
    }

    public void createSatellite(String satelliteId, String type, double height, Angle position) {
        if (type.equals("StandardSatellite")) {
           StandardSatellite newStandard = new StandardSatellite(satelliteId, type, height, position);
            satellites.put(satelliteId, newStandard);
        } else if (type.equals("TeleportingSatellite")) {
            TeleportingSatellite newTeleporting = new TeleportingSatellite(satelliteId, type, height, position);
            satellites.put(satelliteId, newTeleporting);
        } else if (type.equals("RelaySatellite")) {
           RelaySatellite newRelay = new RelaySatellite(satelliteId, type, height, position);
            satellites.put(satelliteId, newRelay);
        } else if (type.equals("ElephantSatellite")) {
            ElephantSatellite newElephant = new ElephantSatellite(satelliteId, type, height, position);
            satellites.put(satelliteId, newElephant);
        }
    }

    public void removeSatellite(String satelliteId) {
        satellites.remove(satelliteId);
    }

    public List<String> listDeviceIds() {
        List<String> allDeviceIds = new ArrayList<String>(devices.keySet());
        return allDeviceIds;
    }

    public List<String> listSatelliteIds() {
        List<String> allSatelliteIds = new ArrayList<String>(satellites.keySet());
        return allSatelliteIds;
    }

    public void addFileToDevice(String deviceId, String filename, String content) {
        FileInfoResponse fileInfo = new FileInfoResponse(filename, content, content.length(), true);
        devices.get(deviceId).addNewMap(filename, fileInfo);
    }

    //helper function
    public boolean checkentity(String id) {
        return devices.containsKey(id);
    }

    public EntityInfoResponse getInfo(String id) {
        if (checkentity(id)) {
            EntityInfoResponse newEntity = new EntityInfoResponse(id,
            devices.get(id).getPosition(), RADIUS_OF_JUPITER,
            devices.get(id).getType(), devices.get(id).getNewMap());
            return newEntity;
        }
       if (!checkentity(id)) {
            EntityInfoResponse newEntity = new EntityInfoResponse(id, satellites.get(id).getPosition(),
            satellites.get(id).getHeight(), satellites.get(id).getType(), satellites.get(id).getNewMap());
            return newEntity;
        }
        return null;
    }

    public void simulate() {
        for (AllSatellite satellite : satellites.values()) {
            satellite.updatePositon();
        }
        updateFileTransfering();
    }

    //helper function
    public FileInfoResponse newFileInfo(TransferingFiles t, String string) {
        FileInfoResponse finishFile = new FileInfoResponse(t.getFilename(), string,
        string.length(), true);
        return finishFile;
    }

    //helper function
    public void updateFileTransfering() {
        for (int i = 0; i < transfer.size(); i++) {
            TransferingFiles t = transfer.get(i);
            String fromId = t.getFromId();
            String toId = t.getToId();
            int fromsize = t.getFilesize();
            if (checkentity(fromId) && !checkentity(toId)) {
                if (!devices.containsKey(fromId) || !satellites.containsKey(toId)) {
                    transfer.remove(t);
                    i -= 1;
                    continue;
                }
                AllSatellite satellite = satellites.get(toId);
                int speed = satellite.getReceivespeed()
                / satellite.getReceiveFilecount();
                int tosize = getInfo(toId).getFiles().get(t.getFilename()).getData().length();
                if (satellite.getType().equals("TeleportingSatellite")) {
                    if (satellite.getPosition().toDegrees() == 0) {
                        satellite.removeFile(t.getFilename());
                        String newString = satellite.updateData(t.getContent());
                        FileInfoResponse finishFile = newFileInfo(t, newString);
                        devices.get(fromId).removeFile(t.getFilename());
                        devices.get(fromId).addNewMap(t.getFilename(), finishFile);
                        transfer.remove(t);
                        i -= 1;
                        continue;
                    }
                }
                if (!communicableEntitiesInRange(fromId).contains(toId)) {
                    satellite.removeFile(t.getFilename());
                    transfer.remove(t);
                    i -= 1;
                    continue;
                }
                if (fromsize - tosize <= speed) {
                    FileInfoResponse finishFile = new FileInfoResponse(t.getFilename(), t.getContent(), fromsize, true);
                    satellite.addNewMap(t.getFilename(), finishFile);
                    transfer.remove(t);
                    i -= 1;
                    continue;
                }
                String string = null;
                string = t.getContent().substring(0,  tosize + speed);
                FileInfoResponse noFinishFile = new FileInfoResponse(t.getFilename(),
                string, fromsize, false);
                satellite.addNewMap(t.getFilename(), noFinishFile);
            }

            if (!checkentity(fromId) && checkentity(toId)) {
                if (!devices.containsKey(toId) || !satellites.containsKey(fromId)) {
                    transfer.remove(t);
                    continue;
                }
                AllSatellite satellite = satellites.get(fromId);
                int speed = satellite.getSendspeed()
                / satellite.getSendFilecount();
                int tosize = getInfo(toId).getFiles().get(t.getFilename()).getData().length();
                if (satellite.getType().equals("TeleportingSatellite")) {
                    if (satellite.getPosition().toDegrees() == 0) {
                        String old = t.getContent().substring(0, tosize);
                        String newString = satellite.updateData(t.getContent()
                        .substring(tosize, t.getFilesize()));
                        String all = old + newString;
                        FileInfoResponse finishFile = new FileInfoResponse(t.getFilename(), all, all.length(), true);
                        devices.get(toId).addNewMap(t.getFilename(), finishFile);
                        transfer.remove(t);
                        continue;
                    }
                }
                if (!communicableEntitiesInRange(fromId).contains(toId)) {
                    devices.get(toId).removeFile(t.getFilename());
                    transfer.remove(t);
                    continue;
                }
                if (fromsize - tosize <= speed) {
                    FileInfoResponse finishFile = new FileInfoResponse(t.getFilename(), t.getContent(), fromsize, true);
                    devices.get(toId).addNewMap(t.getFilename(), finishFile);
                    transfer.remove(t);
                    continue;
                }
                String string = null;
                string = t.getContent().substring(0,  tosize + speed);
                FileInfoResponse files = new FileInfoResponse(t.getFilename(), string, fromsize, false);
                devices.get(toId).addNewMap(t.getFilename(), files);
            }

            if (!checkentity(fromId) && !checkentity(toId)) {
                if (!satellites.containsKey(fromId) || !satellites.containsKey(toId)) {
                    transfer.remove(t);
                    continue;
                }
                AllSatellite satelliteFrom = satellites.get(fromId);
                AllSatellite satelliteTo = satellites.get(toId);
                int speed = Math.min(satelliteFrom.getSendspeed() / satelliteFrom.getSendFilecount(),
                satelliteTo.getReceivespeed() / satelliteTo.getReceiveFilecount());
                int tosize = getInfo(toId).getFiles().get(t.getFilename()).getData().length();
                if (satelliteFrom.getType().equals("TeleportingSatellite")) {
                    if (satelliteFrom.getPosition().toDegrees() == 0) {
                        String old = t.getContent().substring(0, tosize);
                        String newString = satelliteFrom.updateData(t.getContent()
                        .substring(tosize, t.getFilesize()));
                        String all = old + newString;
                        FileInfoResponse finishFile = new FileInfoResponse(t.getFilename(), all, all.length(), true);
                        devices.get(toId).addNewMap(t.getFilename(), finishFile);
                        transfer.remove(t);
                        continue;
                    }
                }
                if (!communicableEntitiesInRange(fromId).contains(toId)) {
                    satellites.get(toId).removeFile(t.getFilename());
                    transfer.remove(t);
                    continue;
                }
                if (fromsize - tosize <= speed) {
                    FileInfoResponse finishFile = new FileInfoResponse(t.getFilename(), t.getContent(), fromsize, true);
                    satellites.get(toId).addNewMap(t.getFilename(), finishFile);
                    transfer.remove(t);
                    continue;
                }
                String string = null;
                string = t.getContent().substring(0,  tosize + speed);
                FileInfoResponse files = new FileInfoResponse(t.getFilename(), string, fromsize, false);
                satellites.get(toId).addNewMap(t.getFilename(), files);
            }
        }

        for (String a : satellites.keySet()) {
            int sendcount = 0;
            int receivecount = 0;
            for (TransferingFiles t : transfer) {
                if (a.equals(t.getToId())) {
                    receivecount += 1;
                }
                if (a.equals(t.getFromId())) {
                    sendcount += 1;
                }
            }
            satellites.get(a).setSendFilecount(sendcount);
            satellites.get(a).setReceiveFilecount(receivecount);
        }
    }

    /**
     * Simulate for the specified number of minutes.
     * You shouldn't need to modify this function.
     */
    public void simulate(int numberOfMinutes) {
        for (int i = 0; i < numberOfMinutes; i++) {
            simulate();
        }
    }

    //helper function
    public double satelliteDistance(String id, AllSatellite visitEntities) {
        return MathsHelper.getDistance(satellites.get(id).getHeight(), satellites.get(id).getPosition(),
        visitEntities.getHeight(), visitEntities.getPosition());
    }

    //helper function
    public boolean isSatelliteInRange(String id, AllSatellite visitEntities) {
        return MathsHelper.isVisible(satellites.get(id).getHeight(), satellites.get(id).getPosition(),
        visitEntities.getHeight(), visitEntities.getPosition());
    }

    //helper function
    public boolean issupportDevice(String id, AllDevice device) {
        if (satellites.get(id).getType().equals("StandardSatellite") && device.getType().equals("DesktopDevice")) {
            return false;
        }
        return true;
    }

    //helper functions
    public boolean issupportSatellite1(String id, AllSatellite satellite) {
        if (satellites.get(id).getType().equals("RelaySatellite") && satellite.getType().equals("StandardSatellite")) {
            return false;
        }
        return true;
    }

    //helper function
    public boolean issupportSatellite(String id, AllSatellite satellite) {
        if (devices.get(id).getType().equals("DesktopDevice") && satellite.getType().equals("StandardSatellite")) {
            return false;
        }
        return true;
    }

    //helper function
    public List<String> satelliteToOtherIds(String id, Map<String, AllSatellite> newmap) {
        List<String> entities = new ArrayList<String>();
        for (AllDevice visitEntities : devices.values()) {
            if (MathsHelper.getDistance(satellites.get(id).getHeight(), satellites.get(id).getPosition(),
            visitEntities.getPosition()) <= satellites.get(id).getMaxrange()
            && MathsHelper.isVisible(satellites.get(id).getHeight(),
            satellites.get(id).getPosition(), visitEntities.getPosition())
            && issupportDevice(id, visitEntities)) {
                entities.add(visitEntities.getDeviceId());
            }
        }
        for (AllSatellite visitEntities : newmap.values()) {
            if (satelliteDistance(id, visitEntities) <= satellites.get(id).getMaxrange()
                && isSatelliteInRange(id, visitEntities) && (add || issupportSatellite1(id, visitEntities))) {
                entities.add(visitEntities.getSatelliteId());
                if (visitEntities.getType().equals("RelaySatellite")) {
                    Map<String, AllSatellite> removeMap = new HashMap<String, AllSatellite>(newmap);
                    removeMap.remove(id);
                    entities.addAll(satelliteToOtherIds(visitEntities.getSatelliteId(), removeMap));
                }
            }
        }
        if (satellites.get(id).getType().equals("StandardSatellite")) {
            for (int i = 0; i < entities.size(); i++) {
                try {
                    if (devices.get(entities.get(i)).getType().equals("DesktopDevice")) {
                        entities.remove(i);
                    }
                } catch (Exception e) {
                    System.out.println("");
                }
            }
        }
        return entities;
    }

    //helper function
    public List<String> addSatellites(String id, Map<String, AllSatellite> newmap) {
        List<String> entities = new ArrayList<String>();
        for (AllSatellite visitEntities : newmap.values()) {
            if (satelliteDistance(id, visitEntities) <= satellites.get(id).getMaxrange()
                && isSatelliteInRange(id, visitEntities) && (add || issupportSatellite1(id, visitEntities))) {
                entities.add(visitEntities.getSatelliteId());
                if (visitEntities.getType().equals("RelaySatellite")) {
                    Map<String, AllSatellite> removeMap = new HashMap<String, AllSatellite>(newmap);
                    removeMap.remove(id);
                    entities.addAll(addSatellites(visitEntities.getSatelliteId(), removeMap));
                }
            }
        }
        return entities;
    }

    //helper function
    public List<String> devicetoSatelliteIds(String id) {
        List<String> entities = new ArrayList<String>();
        if (checkentity(id)) {
            if (devices.get(id).getType().equals("DesktopDevice")) {
                add = false;
            }
        }
        for (AllSatellite visitEntities : satellites.values()) {
            if (MathsHelper.getDistance(visitEntities.getHeight(),
            visitEntities.getPosition(), devices.get(id).getPosition()) <= devices.get(id).getMaxrange()
            && MathsHelper.isVisible(visitEntities.getHeight(), visitEntities.getPosition(),
            devices.get(id).getPosition()) && issupportSatellite(id, visitEntities)) {
                entities.add(visitEntities.getSatelliteId());
                if (visitEntities.getType().equals("RelaySatellite")) {
                    entities.addAll(addSatellites(visitEntities.getSatelliteId(), satellites));
                }
            }
        }
        add = true;
        return entities;
    }


    public List<String> communicableEntitiesInRange(String id) {
        List<String> entities = new ArrayList<String>();
        if (!checkentity(id)) {
            entities = satelliteToOtherIds(id, satellites);
            entities.remove(satellites.get(id).getSatelliteId());
        } else if (checkentity(id)) {
            entities = devicetoSatelliteIds(id);
        }
        // remove the same deviceId
        Set<String> setContent = new HashSet<String>(entities);
        List<String> newEntities = new ArrayList<String>(setContent);
        return newEntities;
    }


    public void sendFile(String fileName, String fromId, String toId) throws FileTransferException {
        FileInfoResponse fileFromInfo = getInfo(fromId).getFiles().get(fileName);
        FileInfoResponse fileToInfo = getInfo(toId).getFiles().get(fileName);

        if (fileFromInfo == null || (!fileFromInfo.hasTransferCompleted())) {
            throw new FileTransferException.VirtualFileNotFoundException(fileName);
        }
        if (checkentity(fromId) && !checkentity(toId)) {
            AllSatellite satellite = satellites.get(toId);
            if (!satellite.isreceiving()) {
                throw new FileTransferException.VirtualFileNoBandwidthException(satellite.getSatelliteId()
                 + "has too little bandwidth");
            }
            if (fileToInfo != null) {
                throw new FileTransferException.VirtualFileAlreadyExistsException(fileName);
            }
            if (!satellite.isfilestorage(fileFromInfo.getFileSize()) || !satellite.isSpace()) {
                throw new FileTransferException.VirtualFileNoStorageSpaceException("Max Files Reached");
            }
            FileInfoResponse newFile = new FileInfoResponse(fileName, "", fileFromInfo.getFileSize(), false);
            satellite.addNewMap(fileName, newFile);
            TransferingFiles newTransferFile = new TransferingFiles(fileName,
            fileFromInfo.getData(), fileFromInfo.getFileSize(), fromId, toId);
            transfer.add(newTransferFile);
            //add transfer files
            satellite.addReceiveFile();
            satellite.addCurrentSpace(fileFromInfo.getFileSize());
            satellite.addCurrentFile();

        }
        if (!checkentity(fromId) && checkentity(toId)) {
            AllSatellite satellite = satellites.get(fromId);
            if (!satellite.issending()) {
                throw new FileTransferException.VirtualFileNoBandwidthException(satellite.getSatelliteId()
                 + "has too little bandwidth");
            }
            if (fileToInfo != null) {
                throw new FileTransferException.VirtualFileAlreadyExistsException(fileName);
            }

            FileInfoResponse newFile = new FileInfoResponse(fileName, "", fileFromInfo.getFileSize(), false);
            devices.get(toId).addNewMap(fileName, newFile);
            TransferingFiles newTransferFile = new TransferingFiles(fileName,
            fileFromInfo.getData(), fileFromInfo.getFileSize(), fromId, toId);
            transfer.add(newTransferFile);
            //加发送文件
            satellite.addSendFile();
        }
        if (!checkentity(fromId) && !checkentity(toId)) {
            AllSatellite satelliteFrom = satellites.get(fromId);
            AllSatellite satelliteTo = satellites.get(toId);
            if (!satelliteFrom.issending() || !satelliteTo.isreceiving()) {
                throw new FileTransferException.VirtualFileNoBandwidthException(satelliteFrom.getSatelliteId()
                 + "has too little bandwidth");
            }
            if (fileToInfo != null) {
                throw new FileTransferException.VirtualFileAlreadyExistsException(fileName);
            }
            if (!satelliteTo.isfilestorage(fileFromInfo.getFileSize()) || !satelliteTo.isSpace()) {
                throw new FileTransferException.VirtualFileNoStorageSpaceException("Max Files Reached");
            }
            FileInfoResponse newFile = new FileInfoResponse(fileName, "", fileFromInfo.getFileSize(), false);
            satelliteTo.addNewMap(fileName, newFile);
            TransferingFiles newTransferFile = new TransferingFiles(fileName,
            fileFromInfo.getData(), fileFromInfo.getFileSize(), fromId, toId);
            transfer.add(newTransferFile);
            satelliteTo.addReceiveFile();
            satelliteFrom.addSendFile();
            satelliteTo.addCurrentSpace(fileFromInfo.getFileSize());
            satelliteTo.addCurrentFile();
        }

    }

    public void createDevice(String deviceId, String type, Angle position, boolean isMoving) {
        createDevice(deviceId, type, position);
        // TODO: Task 3
        if (isMoving) {
            System.out.println("");
        }
    }

    public void createSlope(int startAngle, int endAngle, int gradient) {
        // TODO: Task 3
        // If you are not completing Task 3 you can leave this method blank :)
    }
}
