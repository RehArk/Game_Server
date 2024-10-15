package net.vincent_clerc.world;

import net.vincent_clerc.entities.Entity;
import net.vincent_clerc.manager.GameManager;
import net.vincent_clerc.utils.Point;

import java.util.HashMap;

public class ChunkManager {

    public final int chunk_size = 150;

    private final HashMap<Point, Chunk> chunks;
    private final GameManager gameManager;

    public ChunkManager(GameManager gameManager) {
        this.gameManager = gameManager;
        this.chunks = new HashMap<>();
    }

    public void addEntity(Entity entity) {
        this.gameManager.getEntityManager().addEntity(entity);
    }

    public void addChunk(Point p, Chunk c) {

        if(this.chunks.containsKey(p)) {
            return;
        }

        this.chunks.put(p, c);

    }

}
