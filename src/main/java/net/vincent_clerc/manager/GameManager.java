package net.vincent_clerc.manager;

import net.vincent_clerc.entities.Entity;
import net.vincent_clerc.entities.Player;
import net.vincent_clerc.utils.callbacks.GameUpdateCallback;
import net.vincent_clerc.world.ChunkManager;
import net.vincent_clerc.world.WorldGenerator;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GameManager {

    public static final int ticks = 20;
    public static final int deltatime = 1000 / GameManager.ticks; // 20 ticks

    private final ScheduledExecutorService tickExecutor;

    private GameUpdateCallback gameUpdateCallback;

    private final ChunkManager chunkManager;
    private final EntityManager entityManager;

    public GameManager(GameUpdateCallback gameUpdateCallback) {

        this.gameUpdateCallback = gameUpdateCallback;

        this.entityManager = new EntityManager();
        this.chunkManager = new ChunkManager(this);

        this.tickExecutor = Executors.newScheduledThreadPool(1);

    }

    public void initialize() {

        WorldGenerator wg = new WorldGenerator(this.chunkManager);
        wg.build();

        this.tickExecutor.scheduleAtFixedRate(
                this::tick,
                0,
                this.deltatime,
                TimeUnit.MILLISECONDS
        );

    }

    private void tick() {
        this.update();
        this.gameUpdateCallback.call();
    }

    private void update() {
        System.out.println("update " + this.entityManager.getAllEntities().stream().count() + " entities");
        for(Entity entity : this.getAllEntities()) {
            entity.update();
        }
    }

    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    public Iterable<Entity> getAllEntities() {
        return this.entityManager.getAllEntities();
    }

    public Iterable<Player> getAllPlayers() {
        return this.entityManager.getAllPlayers();
    }

    public Map<String, Object> getGameState() {

        Map<String, Object> gameState = new HashMap<>();
        Iterable<Entity> entities = this.getAllEntities();
        gameState.put("entities", entities);

        return gameState;

    }

}
