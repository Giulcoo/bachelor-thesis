package strategies.model;

public class Vector {
    private double x;
    private double y;
    private double z;

    public Vector(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double getX() {
        return x;
    }

    public Vector setX(double x) {
        this.x = x;
        return this;
    }

    public double getY() {
        return y;
    }

    public Vector setY(double y) {
        this.y = y;
        return this;
    }

    public double getZ() {
        return z;
    }

    public Vector setZ(double z) {
        this.z = z;
        return this;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ", " + z + ")";
    }
}
