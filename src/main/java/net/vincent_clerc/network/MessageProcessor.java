package net.vincent_clerc.network;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.vincent_clerc.entities.Player;

public class MessageProcessor {

    private final ObjectMapper objectMapper;

    public MessageProcessor() {
        this.objectMapper = new ObjectMapper();
    }

    public void processMessage(Player player, String message) {

        System.out.println(message);

        try {
            JsonNode rootNode = objectMapper.readTree(message);
            String action = rootNode.path("action").asText();

            switch (action) {
                case "move":
                    this.handleMoveAction(player, rootNode);
                    break;
                default:
                    System.out.println("Action inconnue : " + action);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void handleMoveAction(Player player, JsonNode rootNode) {

        Double x = Double.parseDouble(rootNode.get("x").asText());
        Double z = Double.parseDouble(rootNode.get("z").asText());

        player.setTargetPosition( x, player.getPosition().getY(), z);

        double deltaX = player.getTargetPosition().getX() - player.getPosition().getX();
        double deltaZ =  player.getTargetPosition().getZ() - player.getPosition().getZ();

        double yaw = Math.atan2(deltaX, deltaZ);
        player.setTargetRotation(0.0,  yaw, 0.0);

    }

}