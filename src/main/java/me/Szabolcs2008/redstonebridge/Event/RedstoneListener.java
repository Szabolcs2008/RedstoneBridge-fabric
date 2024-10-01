package me.Szabolcs2008.redstonebridge.Event;

import com.fasterxml.jackson.databind.JsonNode;
import me.Szabolcs2008.redstonebridge.Config.Config;
import me.Szabolcs2008.redstonebridge.RedstoneBridge;
import me.Szabolcs2008.redstonebridge.Util.UpdateData;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.util.thread.ThreadExecutor;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
;

public class RedstoneListener {

    public static void registerEvents() {
        // Register server tick event to process redstone updates
        ServerTickEvents.END_WORLD_TICK.register(RedstoneListener::processRedstoneUpdate);
    }

    private static void processRedstoneUpdate(ServerWorld world) {
        for (BlockPos pos : RedstoneBridge.bridges.validCoordinates) {
            if (world.getRegistryKey() == World.OVERWORLD) {
                BlockState blockState = world.getBlockState(pos);
                if (Config.getConfig().get("debug").asBoolean()) {
                    RedstoneBridge.LOGGER.info(blockState.getBlock().toString()+" @ "+pos.toString());
                }
                if (!blockState.isAir() && (Config.getWhitelistedBlocks().contains(blockState.getBlock().toString()) || !Config.getConfig().get("enable-whitelist").asBoolean())) {
                    int powerLevel = world.getReceivedRedstonePower(pos);
                    boolean powered = powerLevel > 0;

                    for (String name : RedstoneBridge.bridges.listBridges()) {
                        JsonNode bridge = RedstoneBridge.bridges.getBridge(name);
                        if (bridge.get("block-x").asInt() == pos.getX() && bridge.get("block-y").asInt() == pos.getY() && bridge.get("block-z").asInt() == pos.getZ()) {
                            UpdateData thisUpdate = new UpdateData(name, powered, powerLevel, pos);
                            if (RedstoneBridge.lastUpdateStorage.getLastUpdate(name) == null || !thisUpdate.toString().equals(RedstoneBridge.lastUpdateStorage.getLastUpdate(name).toString())) {
                                RedstoneBridge.lastUpdateStorage.setLastUpdate(name, thisUpdate);
                                String json;
                                String mode = bridge.get("mode").asText();
                                if (mode.equalsIgnoreCase("switch")) {
                                    json = "{\"name\": \""+name+"\", \"mode\": \"SWITCH\", \"powered\": "+powered+"}";
                                } else if (mode.equalsIgnoreCase("rgb")) {
                                    String color = Config.getConfig().get("colors").get(String.valueOf(powerLevel)).asText();
                                    json = "{\"name\": \""+name+"\", \"mode\": \"RGB\", \"powered\": "+powered+", \"power-level\": "+powerLevel+", \"color\": \""+color+"\"}";
                                } else if (mode.equalsIgnoreCase("analogue")) {
                                    json = "{\"name\": \""+name+"\", \"mode\": \"ANALOGUE\", \"powered\": "+powered+", \"power-level\": "+powerLevel+"}";
                                } else {
                                    json = "{}";
                                }
                                if (Config.getConfig().get("debug").asBoolean()) {
                                    RedstoneBridge.LOGGER.info(json);
                                }
                                if (!bridge.get("url").isNull()) {
                                    new Thread(() -> sendHttpRequest(bridge.get("url").asText(), json)).start();
                                } else {
                                    if (Config.getConfig().get("debug").asBoolean()) {
                                        RedstoneBridge.LOGGER.info(name+": URL IS NULL!");
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private static void sendHttpRequest(String url, String json) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                RedstoneBridge.LOGGER.warn("HTTP request failed. Response code: " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            RedstoneBridge.LOGGER.error("Error sending HTTP request", e);
        }
    }
}
