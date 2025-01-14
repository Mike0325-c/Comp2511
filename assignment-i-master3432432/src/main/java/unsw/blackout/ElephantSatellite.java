package unsw.blackout;

import unsw.utils.Angle;

public class ElephantSatellite extends AllSatellite {
    private static final double SPEED = 2500;
    private static final double MAXRANGE = 400000;
    private static final int RECEIVESPEED = 20;
    private static final int SENDSPEED = 20;
    private static final int SPACE = 90;
    private static final int FILESTORAGE = Integer.MAX_VALUE;
    private int currentSpace = 0;
    private int currentFile = 0;
    public ElephantSatellite(String satelliteId, String type, double height, Angle position) {
        super(satelliteId, type, height, position);
    }

    @Override
    public double getMaxrange() {
        return MAXRANGE;
    }

    @Override
    public int getReceivespeed() {
        return RECEIVESPEED;
    }

    @Override
    public int getSendspeed() {
        return SENDSPEED;
    }


    public int getSpace() {
        return SPACE;
    }

    @Override
    public int getFilestorage() {
        return FILESTORAGE;
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
