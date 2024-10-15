package net.vincent_clerc.world;

import net.vincent_clerc.entities.Entity;
import net.vincent_clerc.utils.BasicNoise;

import java.util.HashMap;
import java.util.UUID;

public class Chunk {

    private final ChunkManager chunkManager;
    private final HashMap<UUID, Entity> entities;

    private final int x;
    private final int z;

    private final int chunck_subdivision = 4;

    public Chunk(ChunkManager chunkManager, int x, int z) {

        this.chunkManager = chunkManager;
        this.entities = new HashMap<>();

        this.x = x;
        this.z = z;

        this.addAsteroidsClusters();

    }

    private void addAsteroidsClusters() {

        BasicNoise noise = new BasicNoise(x * 31 + z * 14, 0.01f);

        for (int i = 0; i < chunck_subdivision; i++) {
            for (int y = 0; y < chunck_subdivision; y++) {
                if(noise.reducedNoise(i, y) > 1.5) {
                    this.createCluster(i, y);
                }
            }
        }
    }

    private void createCluster(int x, int z) {

        int x_offset = (int) ((double) x / this.chunck_subdivision * chunkManager.chunk_size);
        int z_offset = (int) ((double) z / this.chunck_subdivision * chunkManager.chunk_size);

        new AsteroidCluster(
                this,
                this.x - x_offset,
                this.z - z_offset
        );
    }

    public void addEntity(Entity entity) {
        this.chunkManager.addEntity(entity);
    }

}
