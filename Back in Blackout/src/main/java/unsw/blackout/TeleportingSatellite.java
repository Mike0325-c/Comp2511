package unsw.blackout;

import unsw.utils.Angle;

public class TeleportingSatellite extends AllSatellite {
    private static final double SPEED = 1000;
    private static final double MAXRANGE = 200000;
    private static final int RECEIVESPEED = 15;
    private static final int SENDSPEED = 10;
    private static final int SPACE = 200;
    private static final int FILESTORAGE = Integer.MAX_VALUE;
    private int moveDirection = 1;
    private int currentSpace = 0;
    private int currentFile = 0;
    // Supports all devices
    public TeleportingSatellite(String satelliteId, String type, double height, Angle position) {
        super(satelliteId, type, height, position);
    }


    public static double getSpeed() {
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


    public int getCurrentSpace() {
        return currentSpace;
    }


    public void setCurrentSpace(int currentSpace) {
        this.currentSpace = currentSpace;
    }


    public int getCurrentFile() {
        return currentFile;
    }


    public void setCurrentFile(int currentFile) {
        this.currentFile = currentFile;
    }


    @Override
    public double getMaxrange() {
        return MAXRANGE;
    }


    @Override
    public void updatePositon() {
        Angle angle = Angle.fromRadians(SPEED / this.getHeight());
        if (moveDirection == 1) {
            if (this.getPosition().toDegrees() >= 180) {
                this.setPosition(Angle.fromDegrees(this.getPosition().toDegrees() - 360));
                this.setPosition((this.getPosition().add(angle)));
                if (this.getPosition().toDegrees() >= 180) {
                    this.setPosition(Angle.fromDegrees(0));
                    moveDirection *= -1;
                }
            } else {
                this.setPosition((this.getPosition().add(angle)));
                if (this.getPosition().toDegrees() >= 180) {
                    this.setPosition(Angle.fromDegrees(0));
                    moveDirection *= -1;
                }
            }
        } else if (moveDirection == -1) {
            this.setPosition(this.getPosition().subtract(angle));
            super.transferDegrees();
            if (this.getPosition().toDegrees() <= 180) {
                this.setPosition(Angle.fromDegrees(0));
                moveDirection *= -1;
            }
        }
    }


    @Override
    public boolean isreceiving() {
        return  super.getReceiveFilecount() < RECEIVESPEED; //必须带宽允许并且容量足够，不然抛出异常
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
