package unsw.blackout;

public interface AllMethods {
    public abstract void updatePositon();

    public abstract boolean isreceiving();

    public abstract boolean issending();

    public abstract boolean isfilestorage(int dataSize);

    public abstract void addCurrentSpace(int dataSize);

    public abstract boolean isSpace();

    public abstract int addCurrentFile();

    public abstract double getMaxrange();

    public abstract int getReceivespeed();

    public abstract int getSendspeed();

    public abstract int getFilestorage();
}
