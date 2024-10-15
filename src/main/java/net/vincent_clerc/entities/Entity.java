package net.vincent_clerc.entities;

import net.vincent_clerc.GameServer;
import net.vincent_clerc.manager.GameManager;
import net.vincent_clerc.utils.Point3D;

import java.util.UUID;

public class Entity {

    private final UUID id;

    private final Point3D position = new Point3D();
    private final Point3D rotation = new Point3D();

    protected Point3D targetPosition = new Point3D();
    protected Point3D targetRotation = new Point3D();

    protected double speed = 5;

    public Entity(UUID uuid, double x,double y,double z) {
        this.id = uuid;
        this.position.set(x, y, z);
        this.rotation.set(0d,0d,0d);
    }

    public UUID getId() {
        return this.id;
    }

    public Point3D getPosition() {
        return this.position;
    }

    public void setPosition(double x, double y, double z) {
        this.position.set(x, y, z);
    }

    public Point3D getRotation() {
        return this.rotation;
    }
    public void setRotation(double x, double y, double z) {
        this.rotation.set(x, y, z);
    }

    public String getType() {
        return this.getClass().getSimpleName();
    }

    public Point3D getTargetPosition() {
        return this.targetPosition;
    }
    public void setTargetPosition(Double x, Double y, Double z) {
        this.targetPosition.set(x, y ,z);
    }

    public Point3D getTargetRotation() {
        return this.targetRotation;
    }
    public void setTargetRotation(Double x, Double y, Double z) {
        this.targetRotation.set(x, y, z);
    }

    public void update() {
        this.moveToTarget();
        this.rotateToTarget();
    }

    public void moveToTarget() {

        Point3D target = this.getTargetPosition();
        Point3D currentPosition = this.getPosition();

        if (target.getX() == null || target.getY() == null || target.getZ() == null) {
            return;
        }

        double deltaX = target.getX() - currentPosition.getX();
        double deltaZ = target.getZ() - currentPosition.getZ();
        double distance = Math.sqrt(deltaX * deltaX + deltaZ * deltaZ);

        if (distance < 0.5) {
            this.setTargetPosition(null, null, null);
            return;
        }

        double dirX = (deltaX / distance) * speed * GameManager.deltatime / 1000;
        double dirZ = (deltaZ / distance) * speed * GameManager.deltatime / 1000;

        this.setPosition(currentPosition.getX() + dirX, currentPosition.getY(), currentPosition.getZ()  + dirZ);

    }

    public void rotateToTarget() {

        Point3D targetRotation = this.getTargetRotation();

        if (targetRotation.getX() == null || targetRotation.getY() == null || targetRotation.getZ() == null) {
            return;
        }

        // Récupère les angles de rotation actuels et cibles
        double currentYaw = this.getRotation().getY();
        double targetYaw = targetRotation.getY();

        // Calcul de la différence angulaire minimale en tenant compte du cercle
        double deltaYaw = targetYaw - currentYaw;
        deltaYaw = ((deltaYaw + Math.PI) % (2 * Math.PI)) - Math.PI;

        // Si la différence est suffisamment faible, considère la rotation comme terminée
        if (Math.abs(deltaYaw) < 0.05) {
            this.setTargetRotation(null, null, null);
            return;
        }

        // Interpolation pour une transition douce
        double interpolationSpeed = 0.1; // Ajustez cette valeur pour un changement plus ou moins rapide
        double interpolateDeltaYaw = currentYaw + deltaYaw * interpolationSpeed;

        this.setRotation(0.0,  interpolateDeltaYaw, 0.0);

    }

}
