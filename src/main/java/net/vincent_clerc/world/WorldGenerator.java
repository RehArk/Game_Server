package net.vincent_clerc.world;

import net.vincent_clerc.utils.Point;

public class WorldGenerator {

    private final ChunkManager chunkManager;

    public WorldGenerator(ChunkManager chunkManager) {
        this.chunkManager = chunkManager;
    }

    public void build() {

        int count = 0;

        for(int x = -1; x < 2; x++) {
            for(int z = -1; z < 2; z++) {

                count++;
//                System.out.println(count);

                this.chunkManager.addChunk(new Point(x, z), new Chunk(
                        this.chunkManager,
                        (x * this.chunkManager.chunk_size) + this.chunkManager.chunk_size / 2,
                        (z * this.chunkManager.chunk_size) + this.chunkManager.chunk_size / 2
                ));
            }
        }

//        System.exit(0);

    }

}
