package unsw.blackout;

import unsw.utils.Angle;

public class DesktopDevice extends AllDevice {
    private static final double MAXRANGE = 200000;

    public DesktopDevice(String id, String type, Angle position) {
        super(id, type, position);
    }

    @Override
    public double getMaxrange() {
        return MAXRANGE;
    }
}
