package unsw.blackout;


import java.util.HashMap;

import java.util.Map;

import unsw.response.models.FileInfoResponse;
import unsw.utils.Angle;

public abstract class AllDevice {
    private String deviceId;
    private String type;
    private Angle position;
    private Map<String, FileInfoResponse> newMap = null;
    public AllDevice(String deviceId, String type, Angle position) {
        this.deviceId = deviceId;
        this.type = type;
        this.position = position;
        this.newMap = new HashMap<String, FileInfoResponse>();
    }

    public String getDeviceId() {
        return deviceId;
    }


    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }


    public String getType() {
        return type;
    }


    public void setType(String type) {
        this.type = type;
    }


    public Angle getPosition() {
        return position;
    }


    public void setPosition(Angle position) {
        this.position = position;
    }


    public Map<String, FileInfoResponse> getNewMap() {
        return newMap;
    }


    public void addNewMap(String fileName, FileInfoResponse fileInfo) {
        this.newMap.put(fileName, fileInfo);
    }


    public void removeFile(String fileName) {
        this.newMap.remove(fileName);
    }


    public abstract double getMaxrange();
}
