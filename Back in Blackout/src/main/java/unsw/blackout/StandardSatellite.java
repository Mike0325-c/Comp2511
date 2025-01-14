package unsw.blackout;

import unsw.utils.Angle;

// import unsw.utils.Angle;

public class StandardSatellite extends AllSatellite {
    private static final double SPEED = 2500;
    private static final double MAXRANGE = 150000;
    private static final int RECEIVESPEED = 1;
    private static final int SENDSPEED = 1;
    private static final int SPACE = 80;
    private static final int FILESTORAGE = 3;
    private int currentSpace = 0;
    private int currentFile = 0;
    // Supports handhelds and laptops only (along with other satellites)

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


    public StandardSatellite(String satelliteId, String type, double height, Angle position) {
        super(satelliteId, type, height, position);
    }


    @Override
    public double getMaxrange() {
        return MAXRANGE;
    }


    @Override
    public void updatePositon() {
        Angle angle = Angle.fromRadians(SPEED / this.getHeight());
        this.setPosition((this.getPosition().subtract(angle)));
        super.transferDegrees();
    }


    @Override
    public boolean isreceiving() {
        return  super.getReceiveFilecount() < RECEIVESPEED;
    }


    @Override
    public boolean issending() {
        return super.getSendFilecount() < SENDSPEED;
    }


    @Override
    public boolean isSpace() {
        return currentFile < FILESTORAGE;
    }


    @Override
    public void addCurrentSpace(int dataSize) {
        this.currentSpace += dataSize;
    }


    @Override
    public boolean isfilestorage(int dataSize) {
        return (SPACE - currentSpace) >=  dataSize;
    }


    @Override
    public int addCurrentFile() {
        return currentFile += 1;
    }

}
