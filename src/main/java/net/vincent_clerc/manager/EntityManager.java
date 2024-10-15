package net.vincent_clerc.manager;

import net.vincent_clerc.entities.Entity;
import net.vincent_clerc.entities.Player;

import java.nio.channels.SocketChannel;
import java.util.*;


public class EntityManager {

    private final Map<UUID, Entity> entities;

    private final Map<UUID, Player> players;

    public EntityManager() {
        this.entities = new HashMap<>();
        this.players = new HashMap<>();
    }

    // ---------- Entity ---------- //

    public void addEntity(Entity entity) {
        this.entities.put(entity.getId(), entity);
    }

    public void removeEntity(Entity entity) {
        this.entities.remove(entity.getId());
    }

    public Collection<Entity> getAllEntities() {
        return Collections.unmodifiableCollection(this.entities.values());
    }

    // ---------- PLAYER ---------- //

    public void addPlayer(Player player) {
        this.players.put(player.getId(), player);
        this.addEntity(player);
    }

    public void removePlayer(Player player) {
        this.players.remove(player.getId());
        this.removeEntity(player);
    }

    public Collection<Player> getAllPlayers() {
        return Collections.unmodifiableCollection(this.players.values());
    }

    public Player getPlayerByChannel(SocketChannel channel) {
        return players.values()
                .stream()
                .filter(player -> player.getSocket().equals(channel))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Joueur non trouvé pour le canal spécifié"));
    }

}