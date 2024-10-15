package net.vincent_clerc.world;

import net.vincent_clerc.entities.Asteroid;

import java.util.Random;
import java.util.UUID;

public class AsteroidCluster {

    private final Random random;
    private final Chunk chunk;
    private final int x;
    private final int z;

    public AsteroidCluster(Chunk chunk, int x, int z) {

        this.random = new Random(x + z);
        this.chunk = chunk;
        this.x = x;
        this.z = z;

        this.generateAsteroids();

    }

    public void generateAsteroids() {

        int clusterSize = this.random.nextInt() % 7 + 3;
//        int clusterSize = 1;

        for (int i = 0; i < clusterSize; i++) {
//            this.chunk.addEntity(new Asteroid(UUID.randomUUID(), x, 0, z));

            this.generateAsteroid();
        }

    }

    public void generateAsteroid() {

        double offsetX = this.random.nextInt() % 15 - 7.5;
        double offsetY = this.random.nextInt() % 3 - 4;
        double offsetZ = this.random.nextInt() % 15 - 7.5;

        double x = this.x + offsetX;
        double y = 0 + offsetY * 5 - 10;
        double z = this.z + offsetZ;

        this.chunk.addEntity(new Asteroid(UUID.randomUUID(), x, y, z));

    }

}
