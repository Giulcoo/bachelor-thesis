package strategies.model;

public class Quaternion {
    private double x;
    private double y;
    private double z;
    private double w;

    public Quaternion(double x, double y, double z, double w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public double getX() {
        return x;
    }

    public Quaternion setX(double x) {
        this.x = x;
        return this;
    }

    public double getY() {
        return y;
    }

    public Quaternion setY(double y) {
        this.y = y;
        return this;
    }

    public double getZ() {
        return z;
    }

    public Quaternion setZ(double z) {
        this.z = z;
        return this;
    }

    public double getW() {
        return w;
    }

    public Quaternion setW(double w) {
        this.w = w;
        return this;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ", " + z + ", " + w + ")";
    }
}
