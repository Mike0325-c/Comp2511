package unsw.blackout;

import unsw.utils.Angle;

public class LaptopDevice extends AllDevice {
    private static final double MAXRANGE = 100000;

    public LaptopDevice(String id, String type, Angle position) {
        super(id, type, position);
    }

    @Override
    public double getMaxrange() {
        return MAXRANGE;
    }
}
