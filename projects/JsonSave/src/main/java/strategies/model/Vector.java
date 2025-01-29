package strategies.model;

public class Vector {
    private double x;
    private double y;

    public Vector() {

    }

    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector(Vector vector){
        this.x = vector.getX();
        this.y = vector.getY();
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
}
