package strategies.model;

import java.beans.PropertyChangeSupport;

public class Vector
{
    public static final String PROPERTY_X = "x";
    public static final String PROPERTY_Y = "y";
    public static final String PROPERTY_Z = "z";
    private double x;
    private double y;
    private double z;
    protected PropertyChangeSupport listeners;

    public Vector(double x, double y, double z) {
        this.setX(x);
        this.setY(y);
        this.setZ(z);
    }

    public double getX()
    {
        return this.x;
    }

    public Vector setX(double value)
    {
        if (value == this.x)
        {
            return this;
        }

        final double oldValue = this.x;
        this.x = value;
        this.firePropertyChange(PROPERTY_X, oldValue, value);
        return this;
    }

    public double getY()
    {
        return this.y;
    }

    public Vector setY(double value)
    {
        if (value == this.y)
        {
            return this;
        }

        final double oldValue = this.y;
        this.y = value;
        this.firePropertyChange(PROPERTY_Y, oldValue, value);
        return this;
    }

    public double getZ()
    {
        return this.z;
    }

    public Vector setZ(double value)
    {
        if (value == this.z)
        {
            return this;
        }

        final double oldValue = this.z;
        this.z = value;
        this.firePropertyChange(PROPERTY_Z, oldValue, value);
        return this;
    }

    public Vector add(Vector other) {
        setX(getX() + other.getX());
        setY(getY() + other.getY());
        setZ(getZ() + other.getZ());
        return this;
    }

    public boolean firePropertyChange(String propertyName, Object oldValue, Object newValue)
    {
        if (this.listeners != null)
        {
            this.listeners.firePropertyChange(propertyName, oldValue, newValue);
            return true;
        }
        return false;
    }

    public PropertyChangeSupport listeners()
    {
        if (this.listeners == null)
        {
            this.listeners = new PropertyChangeSupport(this);
        }
        return this.listeners;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ", " + z + ")";
    }
}