package strategies.old.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;

import java.beans.PropertyChangeSupport;

public class Vector
{
    @JsonIgnore public static final String PROPERTY_X = "x";
    @JsonIgnore public static final String PROPERTY_Y = "y";
    @JsonIgnore public static final String PROPERTY_Z = "z";
    private double x;
    private double y;
    private double z;
    @JsonIgnore protected PropertyChangeSupport listeners;

    public Vector(double x, double y, double z) {
        this.setX(x);
        this.setY(y);
        this.setZ(z);
    }

    public Vector(JsonNode node){
        this.setX(node.get("x").asDouble());
        this.setY(node.get("y").asDouble());
        this.setZ(node.get("z").asDouble());
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

    public String toString2D(){
        return "(" + x + "," + y + ")";
    }

    @Override
    public String toString() {
        return "(" + (double) Math.round(x * 10d) / 10d + ", " + (double) Math.round(y * 10d) / 10d + ", " + (double) Math.round(z * 10d) / 10d + ")";
    }

    public Vector copy() {
        return new Vector(x, y, z);
    }
}