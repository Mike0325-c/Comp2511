package unsw.blackout;

import java.util.HashMap;
import java.util.Map;
import unsw.response.models.FileInfoResponse;
import unsw.utils.Angle;

public  abstract class AllSatellite implements AllMethods {
    private String satelliteId;
    private String type;
    private double height;
    private Angle position;
    private int sendFilecount = 0;
    private int receiveFilecount = 0;
    private Map<String, FileInfoResponse> newMap = null;

    public AllSatellite(String satelliteId, String type, double height, Angle position) {
        this.satelliteId = satelliteId;
        this.type = type;
        this.height = height;
        this.position = position;
        this.newMap = new HashMap<String, FileInfoResponse>();
    }


    public int getSendFilecount() {
        return sendFilecount;
    }


    public void setSendFilecount(int sendFilecount) {
        this.sendFilecount = sendFilecount;
    }


    public int addSendFile() {
       return this.sendFilecount += 1;
    }


    public int subSendFile() {
        return this.sendFilecount -= 1;
    }


    public int getReceiveFilecount() {
        return receiveFilecount;
    }


    public void setReceiveFilecount(int receiveFilecount) {
        this.receiveFilecount = receiveFilecount;
    }


    public int addReceiveFile() {
        return this.receiveFilecount += 1;
    }


    public int subReceiveFile() {
        return this.receiveFilecount -= 1;
    }


    public String getSatelliteId() {
        return satelliteId;
    }


    public void setSatelliteId(String satelliteId) {
        this.satelliteId = satelliteId;
    }


    public String getType() {
        return type;
    }


    public void setType(String type) {
        this.type = type;
    }


    public double getHeight() {
        return height;
    }


    public void setHeight(double height) {
        this.height = height;
    }


    public Angle getPosition() {
        return position;
    }


    public void setPosition(Angle position) {
        this.position = position;
    }


    public void transferDegrees() {
        while (this.getPosition().toDegrees() < 0) {
            this.setPosition(Angle.fromDegrees(360 + this.getPosition().toDegrees()));
        }
        while (this.getPosition().toDegrees() >= 360) {
            this.setPosition(Angle.fromDegrees(this.getPosition().toDegrees() - 360));
        }
    }

    public void addNewMap(String fileName, FileInfoResponse fileInfo) {
        this.newMap.put(fileName, fileInfo);
    }


    public void removeFile(String fileName) {
        this.newMap.remove(fileName);
    }


    public Map<String, FileInfoResponse> getNewMap() {
        return newMap;
    }


    public String updateData(String data) {
        return data.replace("t", "");
    }
}

