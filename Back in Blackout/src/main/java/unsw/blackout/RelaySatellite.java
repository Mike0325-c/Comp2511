package unsw.blackout;

import unsw.utils.Angle;

public class RelaySatellite extends AllSatellite {
    private static final double SPEED = 1500;
    private static final double MAXRANGE =  300000;
    private static final int RECEIVESPEED = 0;
    private static final int SENDSPEED = 0;
    private static final int SPACE = 0;
    private static final int FILESTORAGE = 0;
    private int moveDirection = -1;
    private int currentSpace = 0;
    private int currentFile = 0;

    public double getSpeed() {
        return SPEED;
    }


    public int getReceivespeed() {
        return RECEIVESPEED;
    }


    public int getSendspeed() {
        return SENDSPEED;
    }


    public int getSpace() {
        return SPACE;
    }


    public int getFilestorage() {
        return FILESTORAGE;
    }


    public int getCurrentFile() {
        return currentFile;
    }


    public void setCurrentFile(int currentFile) {
        this.currentFile = currentFile;
    }


    public RelaySatellite(String satelliteId, String type, double height, Angle position) {
        super(satelliteId, type, height, position);
    }


    @Override
    public double getMaxrange() {
        return MAXRANGE;
    }


    @Override
    public void updatePositon() {
       Angle a = Angle.fromRadians(SPEED / this.getHeight());
       if (this.getPosition().toDegrees() >= 345 && this.getPosition().toDegrees() <= 360
       || this.getPosition().toDegrees() >= 0 && this.getPosition().toDegrees() <= 140) {
            this.setPosition((this.getPosition().add(a)));
            super.transferDegrees();
            moveDirection = 1;
       } else if (this.getPosition().toDegrees() < 345 && this.getPosition().toDegrees() >= 190) {
            this.setPosition((this.getPosition().subtract(a)));
            moveDirection = -1;
       } else {
            if (moveDirection == 1 && (this.getPosition().toDegrees() <= 190
             && this.getPosition().toDegrees() >= 140)) {
                this.setPosition((this.getPosition().add(a)));
            } else if (moveDirection == -1 && (this.getPosition().toDegrees() <= 190
            && this.getPosition().toDegrees() >= 140)) {
                this.setPosition((this.getPosition().subtract(a)));
            }
       }
    }


    @Override
    public boolean isreceiving() {
        return  super.getReceiveFilecount() < RECEIVESPEED;
    }


    @Override
    public boolean issending() {
        return this.getSendFilecount() < SENDSPEED;
    }


    @Override
    public boolean isfilestorage(int dataSize) {
        return (SPACE - currentSpace) >=  dataSize;
    }


    @Override
    public void addCurrentSpace(int dataSize) {
        this.currentSpace += dataSize;
    }


    @Override
    public boolean isSpace() {
        return currentFile < FILESTORAGE;
    }


    @Override
    public int addCurrentFile() {
        return currentFile += 1;
    }
}
