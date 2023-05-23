package strategies.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.beans.PropertyChangeSupport;

public class Quaternion
{
    @JsonIgnore public static final String PROPERTY_X = "x";
    @JsonIgnore public static final String PROPERTY_Y = "y";
    @JsonIgnore public static final String PROPERTY_Z = "z";
    @JsonIgnore public static final String PROPERTY_W = "w";
    private double x;
    private double y;
    private double z;
    private double w;
    @JsonIgnore protected PropertyChangeSupport listeners;

    public Quaternion(double x, double y, double z, double w) {
        this.setX(x);
        this.setY(y);
        this.setZ(z);
        this.setW(w);
    }

    public double getX()
    {
        return this.x;
    }

    public Quaternion setX(double value)
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

    public Quaternion setY(double value)
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

    public Quaternion setZ(double value)
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

    public double getW()
    {
        return this.w;
    }

    public Quaternion setW(double value)
    {
        if (value == this.w)
        {
            return this;
        }

        final double oldValue = this.w;
        this.w = value;
        this.firePropertyChange(PROPERTY_W, oldValue, value);
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
        return "(" + x + ", " + y + ", " + z + ", " + w + ")";
    }

    public Quaternion copy() {
        return new Quaternion(x, y, z, w);
    }
}
