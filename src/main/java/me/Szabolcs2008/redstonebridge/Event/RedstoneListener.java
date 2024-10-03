package me.Szabolcs2008.redstonebridge.Event;

import com.fasterxml.jackson.databind.JsonNode;
import me.Szabolcs2008.redstonebridge.Config.Bridges;
import me.Szabolcs2008.redstonebridge.Config.Config;
import me.Szabolcs2008.redstonebridge.RedstoneBridge;
import me.Szabolcs2008.redstonebridge.Util.UpdateData;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
;

public class RedstoneListener {

    public static void registerEvents() {
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
                    new Thread(() -> {
                        int powerLevel = world.getReceivedRedstonePower(pos);
                        boolean powered = powerLevel > 0;

                        for (String name : RedstoneBridge.bridges.listBridges()) {
                            JsonNode bridge = RedstoneBridge.bridges.getBridge(name);
                            if (bridge.get(Bridges.ENABLED).asBoolean() && bridge.get(Bridges.X).asInt() == pos.getX() && bridge.get(Bridges.Y).asInt() == pos.getY() && bridge.get(Bridges.Z).asInt() == pos.getZ()) {
                                UpdateData thisUpdate = new UpdateData(name, powerLevel, pos);
                                if (RedstoneBridge.lastUpdateStorage.getLastUpdate(name) == null || !thisUpdate.toString().equals(RedstoneBridge.lastUpdateStorage.getLastUpdate(name).toString())) {
                                    RedstoneBridge.lastUpdateStorage.setLastUpdate(name, thisUpdate);
                                    String json;
                                    String mode = bridge.get("mode").asText();
                                    if (mode.equalsIgnoreCase(Bridges.DIGITAL.toLowerCase())) {
                                        int state = powered? 1 : 0;
                                        json = "{\"bridgeName\": \""+name+"\", \"bridgeType\": \""+Bridges.DIGITAL+"\", \"data\": "+state+"}";
                                    } else if (mode.equalsIgnoreCase(Bridges.RGB.toLowerCase())) {
                                        String color = Config.getConfig().get("colors").get(String.valueOf(powerLevel)).asText();
                                        json = "{\"bridgeName\": \""+name+"\", \"bridgeType\": \""+Bridges.RGB+"\", \"data\": \""+color+"\"}";
                                    } else if (mode.equalsIgnoreCase(Bridges.ANALOGUE.toLowerCase())) {
                                        json = "{\"bridgeName\": \""+name+"\", \"bridgeType\": \""+Bridges.ANALOGUE+"\", \"data\": "+powerLevel+"}";
                                    } else {
                                        json = "{}";
                                    }
                                    if (Config.getConfig().get("debug").asBoolean()) {
                                        RedstoneBridge.LOGGER.info(json);
                                    }
                                    if (!bridge.get("url").isNull()) {
                                        sendHttpRequest(bridge.get("url").asText(), json);
                                    } else {
                                        if (Config.getConfig().get("debug").asBoolean()) {
                                            RedstoneBridge.LOGGER.info(name+": URL IS NULL!");
                                        }
                                    }
                                }
                            }
                        }
                    }).start();
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
