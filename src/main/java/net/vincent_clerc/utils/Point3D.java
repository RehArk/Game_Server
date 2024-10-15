package net.vincent_clerc.utils;

public class Point3D {

    Double x;
    Double y;
    Double z;

    public Point3D() {
    }

    public Point3D(Double x, Double y, Double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Double getX() {
        return x;
    }

    public Double getY() {
        return y;
    }

    public Double getZ() {
        return z;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public void setY(Double y) {
        this.y = y;
    }

    public void setZ(Double z) {
        this.z = z;
    }

    public void set(Double x, Double y, Double z) {
        this.setX(x);
        this.setY(y);
        this.setZ(z);
    }

}
