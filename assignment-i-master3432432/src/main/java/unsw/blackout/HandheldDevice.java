package unsw.blackout;

import unsw.utils.Angle;

public class HandheldDevice extends AllDevice {
    public static final double MAXRANGE = 50000;

    public HandheldDevice(String id, String type, Angle position) {
        super(id, type, position);
    }

    @Override
    public double getMaxrange() {
        return MAXRANGE;
    }
}
